CREATE OR REPLACE VIEW financial.closing_results
AS select
	row_number() OVER () AS id, fp.id financial_period_id, fp.identification financial_period, c.closing_date closing_date,
	c.expenses expenses, c.revenues revenues, c.credit_card_expenses credit_card_expenses,
	c.debit_card_expenses debit_card_expenses, c.cash_expenses cash_expenses, c.balance balance, c.accumulated accumulated
from financial.closings c
	inner join registration.financial_periods fp on c.id_financial_period = fp.id
order by c.closing_date desc;