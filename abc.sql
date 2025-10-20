--lệnh if exists để xóa database nếu đã tồn tại trước đó 
USE master;
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'UserDB')
BEGIN
    ALTER DATABASE UserDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE UserDB;
END;
--Tạo database
create database UserDB
go
use UserDB
go

CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL,  -- ← Thêm cột này (độ dài phù hợp)
    email NVARCHAR(100) NOT NULL UNIQUE,
    password NVARCHAR(100) NOT NULL
);
INSERT INTO Users (username, email, password)
VALUES 
('test_user', 'khangheheqt@gmail.com', '123456'),
('user1', 'user1@gmail.com', 'abc123'),
('user2', 'user2@gmail.com', 'mypassword');
GO

-- 5️⃣ Kiểm tra dữ liệu
SELECT * FROM Users;
GO