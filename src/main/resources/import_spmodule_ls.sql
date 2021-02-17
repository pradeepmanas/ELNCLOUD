drop procedure sp_LsModuleName
----------------------------------
Create Procedure sp_LsModuleName
as

begin 

SET NOCOUNT ON;

		Create Table #TempTableModule (mModuleName nvarchar(50))
		Insert into #TempTableModule values (N'All')
        Insert into #TempTableModule values (N'Audit Trail')
        Insert into #TempTableModule values (N'CFR Settings')
        Insert into #TempTableModule values (N'Login')
		--Insert into #TempTableModule values (N'Login')
        Insert into #TempTableModule values (N'Masters')
        Insert into #TempTableModule values (N'Order Processing')
        Insert into #TempTableModule values (N'Sheet Creation')
        Insert into #TempTableModule values (N'Sheet Settings')
        Insert into #TempTableModule values (N'User Management')
        Insert into #TempTableModule values (N'User Master')
		--Insert into #TempTableModule values (N'Workflow')


		select mModuleName as ModuleName from #TempTableModule
				
end