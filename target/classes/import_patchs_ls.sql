select 1

select * from LSSitemaster

ALTER TABLE LSsamplefileversion ALTER COLUMN filecontent TYPE text

update lsusermaster set lockcount = 0 where lockcount = 5 and userstatus != 'Locked'