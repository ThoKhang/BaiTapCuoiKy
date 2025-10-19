CREATE DATABASE UserDB;
GO
USE UserDB;
GO

CREATE TABLE Users (
    id INT IDENTITY(1,1) PRIMARY KEY,
    username NVARCHAR(50) NOT NULL,  -- ← Thêm cột này (độ dài phù hợp)
    email NVARCHAR(100) NOT NULL UNIQUE,
    password NVARCHAR(100) NOT NULL
);
INSERT INTO Users (username, email, password)
VALUES 
('test_user', 'test@example.com', '123456'),
('user1', 'user1@gmail.com', 'abc123'),
('user2', 'user2@gmail.com', 'mypassword');
GO

-- 5️⃣ Kiểm tra dữ liệu
SELECT * FROM Users;
GO