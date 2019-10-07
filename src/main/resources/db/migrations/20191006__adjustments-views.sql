DROP VIEW financial.wb_view_009;

CREATE OR REPLACE VIEW financial.wb_view_009 AS
SELECT row_number() OVER () AS id,
       fp.id AS financial_period_id,
       fp.identification AS financial_period,
       cc.id AS cost_center_id,
       cc.color AS cost_center_color,
       cc.name AS cost_center,
       mc.id AS movement_class_id,
       mc.name AS movement_class,
       mc.movement_class_type AS direction,
       sum(pa.paid_value) AS total_paid
FROM financial.movements pm
         JOIN financial.payments pa ON pm.id_payment = pa.id
         JOIN financial.apportionments ap ON ap.id_movement = pm.id
         JOIN registration.cost_centers cc ON ap.id_cost_center = cc.id
         JOIN registration.movement_classes mc ON ap.id_movement_class = mc.id
         JOIN registration.financial_periods fp ON pm.id_financial_period = fp.id AND pm.discriminator_value::text = 'PERIOD_MOVEMENT'::text AND pm.period_movement_type::text = 'MOVEMENT'::text
GROUP BY fp.id, fp.identification, cc.id, cc.color, cc.name, mc.id, mc.name, mc.movement_class_type
ORDER BY fp.identification, cc.name, mc.name;

COMMENT ON VIEW financial.wb_view_009 IS 'Daily consumption grouped by cost center and movement class';