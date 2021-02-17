open symmetric key LSUserMaster decryption by certificate AG_LDMS
update LSUserMaster set L02Password = encryptbykey(key_guid('LSUserMaster'),N'admin' , 3, L02UserID), 
L02PasswordExpiryDate=null 
where 
L02SiteCode='MAA' and 
L02UserID = 'U1'
close all symmetric keys

--DROP SYMMETRIC KEY L02UserMaster; 
--DROP SYMMETRIC KEY LSUserMaster; 
--DROP CERTIFICATE AG_LDMS;
--DROP MASTER KEY;
--------------------------------------------------------------------
create master key encryption by password = 'LDMSmaraga@123'

create certificate AG_LDMS with subject = 'Password For User Master'
 
create symmetric key L02UserMaster with algorithm = aes_192 encryption by certificate AG_LDMS
create symmetric key LSUserMaster with algorithm = aes_192 encryption by certificate AG_LDMS

select * from LSUserMaster
--------------------------------------------------------------------
--LogIn License
OPEN SYMMETRIC KEY  LSUserMaster DECRYPTION BY CERTIFICATE  AG_LDMS;
update LSControl set L60Licence = encryptbykey(key_guid('LSUserMaster'),N'1', 3, L60ControlID)
where L60SiteCode = N'MAA'
