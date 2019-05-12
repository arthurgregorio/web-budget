DROP VIEW financial.wb_view_001;
DROP VIEW financial.wb_view_011;

-- recreate view 011
CREATE OR REPLACE VIEW financial.wb_view_011 AS
    WITH revenues_total AS (
        SELECT fp.id AS period_id,
               COALESCE(sum(pm.paid_value), 0::numeric) AS revenues
        FROM financial.movements mv
                 JOIN financial.apportionments ap ON ap.id_movement = mv.id
                 JOIN financial.payments pm ON pm.id = mv.id_payment
                 JOIN registration.financial_periods fp ON fp.id = mv.id_financial_period
                 JOIN registration.movement_classes mc ON mc.id = ap.id_movement_class AND fp.closed = false AND mv.discriminator_value::text = 'PERIOD_MOVEMENT'::text AND mc.movement_class_type::text = 'REVENUE'::text
        GROUP BY fp.id
    ), expenses_total AS (
        SELECT fp.id AS period_id,
               COALESCE(sum(pm.paid_value), 0::numeric) AS expenses
        FROM financial.movements mv
                 JOIN financial.apportionments ap ON ap.id_movement = mv.id
                 JOIN financial.payments pm ON pm.id = mv.id_payment
                 JOIN registration.financial_periods fp ON fp.id = mv.id_financial_period
                 JOIN registration.movement_classes mc ON mc.id = ap.id_movement_class AND fp.closed = false AND mv.period_movement_type::text = 'MOVEMENT'::text AND mv.discriminator_value::text = 'PERIOD_MOVEMENT'::text AND mc.movement_class_type::text = 'EXPENSE'::text
        GROUP BY fp.id
    ), open_financial_periods AS (
        SELECT fp.id AS financial_period_id,
               fp.identification AS financial_period,
               fp.expired,
               ext.expenses,
               rvt.revenues
        FROM registration.financial_periods fp
                 JOIN expenses_total ext ON ext.period_id = fp.id
                 JOIN revenues_total rvt ON rvt.period_id = fp.id
    )
    SELECT row_number() OVER () AS id,
           ofp.financial_period_id,
           ofp.financial_period,
           ofp.expired,
           ofp.revenues,
           ofp.expenses,
           sum(ofp.revenues - ofp.expenses) AS balance
    FROM open_financial_periods ofp
    GROUP BY ofp.financial_period_id, ofp.financial_period, ofp.expired, ofp.revenues, ofp.expenses;

COMMENT ON VIEW financial.wb_view_011 IS 'Quick resume of the current open period, excluding expired ones';

-- recreate view 001
CREATE OR REPLACE VIEW financial.wb_view_001 AS
SELECT row_number() OVER () AS id,
       mc.movement_class_type AS direction,
       cc.id AS cost_center_id,
       cc.name AS cost_center,
       cc.color AS cost_center_color,
       COALESCE(sum(pa.paid_value), 0::numeric) AS total_value
FROM financial.movements pm
         JOIN registration.financial_periods fp ON fp.id = pm.id_financial_period
         JOIN financial.apportionments ap ON ap.id_movement = pm.id
         JOIN registration.cost_centers cc ON cc.id = ap.id_cost_center
         JOIN registration.movement_classes mc ON mc.id = ap.id_movement_class
         JOIN financial.payments pa ON pa.id = pm.id_payment AND fp.closed = false AND pm.period_movement_type::text = 'MOVEMENT'::text AND pm.discriminator_value::text = 'PERIOD_MOVEMENT'::text
GROUP BY cc.id, cc.name, mc.movement_class_type
ORDER BY mc.movement_class_type;

COMMENT ON VIEW financial.wb_view_001 IS 'List by the open financial periods all cost centers and the respective value spent or received in each one';


