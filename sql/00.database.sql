USE master;
GO

-- Check if the bbpet database exists
IF EXISTS (SELECT * FROM sys.databases WHERE name = 'warehousesys')
    BEGIN
        -- Delete the bbpet database
        ALTER DATABASE warehousesys SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
        DROP DATABASE warehousesys;
    END
GO

-- Create a new bbpet database
CREATE DATABASE warehousesys;
GO