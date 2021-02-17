drop procedure sp_CreateArchiveAuditLSDB
-----------------------------------------------------

create Procedure sp_CreateArchiveAuditLSDB
as

begin

SET NOCOUNT ON;

------------------------------------------------
------ Decision to take ----------------------------
---- Checked by Anand --------------------------
------------------------------------------------


Declare @CreateDB nvarchar(Max)
Declare @CreateDBName nvarchar(500)
Declare @CreateDBLog nvarchar(500)
Declare @DBPath nvarchar(2000)
Declare @DBFullPathMDF nvarchar(2000)
Declare @DBFullPathLDF nvarchar(2000)


select @CreateDBName = L42ValueSettings from LSPreferences where L42SerialNo = 1

select @DBPath = Left(physical_name, LEN(physical_name) - CHARINDEX(N'\', REVERSE(physical_name)))
FROM sys.master_files where database_id = DB_ID() and name = DB_NAME()


set @DBFullPathMDF = LTRIM(RTRIM(@DBPath))+N'\'+LTRIM(RTRIM(@CreateDBName))+N'.mdf'
set @DBFullPathLDF = LTRIM(RTRIM(@DBPath))+N'\'+LTRIM(RTRIM(@CreateDBName))+N'_log.LDF'
set @CreateDBLog = LTRIM(RTRIM(@CreateDBName))+N'_log'

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N''+LTRIM(RTRIM(@CreateDBName))+'')
BEGIN

set @CreateDB = N'CREATE DATABASE ['+LTRIM(RTRIM(@CreateDBName))+'] ON  PRIMARY 
( NAME = N'''+LTRIM(RTRIM(@CreateDBName))+''', FILENAME = N'''+LTRIM(RTRIM(@DBFullPathMDF))+''', MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'''+LTRIM(RTRIM(@CreateDBLog))+''', FILENAME = N'''+LTRIM(RTRIM(@DBFullPathLDF))+''' , MAXSIZE = 2048GB , FILEGROWTH = 10%)'

exec (@CreateDB)

end

end