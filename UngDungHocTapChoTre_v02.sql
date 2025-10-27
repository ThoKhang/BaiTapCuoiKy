USE master;
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'UngDungHocTapChoTre')
BEGIN
    ALTER DATABASE UngDungHocTapChoTre SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE UngDungHocTapChoTre;
END;
--Tạo database
CREATE DATABASE UngDungHocTapChoTre
GO
USE UngDungHocTapChoTre
GO
-- Vai trò (Admin/Người dùng) (bảng tùy chọn)
CREATE TABLE VaiTro (
    MaVaiTro TINYINT PRIMARY KEY,
    TenVaiTro NVARCHAR(50) NOT NULL UNIQUE
);
INSERT INTO VaiTro (MaVaiTro, TenVaiTro) VALUES (1,'Admin'),(2,'NguoiDung');
GO

-- Người dùng
CREATE TABLE NguoiDung (
    MaNguoiDung INT IDENTITY(1,1) PRIMARY KEY,
    TenDangNhap NVARCHAR(100) NOT NULL UNIQUE,
    Email NVARCHAR(255) UNIQUE,
    MatKhauMaHoa NVARCHAR(512) NOT NULL,
    MaVaiTro TINYINT NOT NULL REFERENCES VaiTro(MaVaiTro),
    NgayTao DATETIME2 DEFAULT SYSUTCDATETIME(),
    LanDangNhapCuoi DATETIME2 NULL,
    SoLanTrucTuyen INT DEFAULT 0, -- số lần trực tuyến (đếm khi đăng nhập)
    TongDiem INT DEFAULT 0 -- tổng điểm để truy vấn nhanh
);
INSERT INTO NguoiDung (TenDangNhap, Email, MatKhauMaHoa, MaVaiTro)
VALUES 
    (N'Thọ Khang', N'khangheheqt@gmail.com', N'123456', 2),
    (N'AdminTest', N'admin@example.com', N'admin123', 1),
    (N'UserOne', N'user1@example.com', N'password1', 2),
    (N'UserTwo', N'user2@example.com', N'password2', 2);
GO

-- Môn học
CREATE TABLE MonHoc (
    MaMonHoc TINYINT PRIMARY KEY,
    TenMonHoc NVARCHAR(100) NOT NULL UNIQUE -- 'Toán', 'Tiếng Việt'
);
INSERT INTO MonHoc (MaMonHoc, TenMonHoc) VALUES (1,'Toán'),(2,'Tiếng Việt');
GO

-- Lý thuyết (lý thuyết cơ bản)
CREATE TABLE LyThuyet (
    MaLyThuyet INT IDENTITY(1,1) PRIMARY KEY,
    MaMonHoc TINYINT NOT NULL REFERENCES MonHoc(MaMonHoc),
    TieuDe NVARCHAR(200) NOT NULL,
    NoiDung NVARCHAR(MAX) NULL,
    NgayTao DATETIME2 DEFAULT SYSUTCDATETIME()
);
INSERT INTO LyThuyet (MaMonHoc, TieuDe, NoiDung) VALUES
(1, N'Cộng số tự nhiên', N'Cộng hai số a + b = c, ví dụ 2 + 3 = 5. Đây là phép tính cơ bản nhất trong Toán học tiểu học.'),
(1, N'Trừ số tự nhiên', N'Trừ hai số a - b = c, ví dụ 5 - 2 = 3. Nhớ rằng a phải lớn hơn b.'),
(1, N'Nhân số tự nhiên', N'Nhân hai số a * b = c, ví dụ 4 * 3 = 12. Sử dụng bảng cửu chương để học nhanh.'),
(2, N'Chữ cái alphabet', N'A B C D E F G H I J K L M N O P Q R S T U V W X Y Z. Học thuộc lòng theo thứ tự.'),
(2, N'Từ ghép', N'Nhà cửa, cây cối, sách vở. Từ ghép là kết hợp hai từ đơn để tạo nghĩa mới.'),
(2, N'Câu đơn', N'Con mèo chạy. Câu đơn có chủ ngữ và vị ngữ.');
GO

-- Bài kiểm tra (đề: củng cố + ôn tập + thử thách)
CREATE TABLE BaiKiemTra (
    MaBaiKiemTra INT IDENTITY(1,1) PRIMARY KEY,
    TieuDe NVARCHAR(200) NOT NULL,
    LoaiBaiKiemTra NVARCHAR(50) NOT NULL, -- 'CungCo','OnTapCoBan','OnTapTrungBinh','OnTapNangCao','ThuThach'
    MaMonHoc TINYINT NULL REFERENCES MonHoc(MaMonHoc), -- NULL nếu hỗn hợp toán + TV
    CoXaoTron BIT DEFAULT 1,
    TongSoCauHoi INT DEFAULT 10,
    NgayTao DATETIME2 DEFAULT SYSUTCDATETIME()
);
INSERT INTO BaiKiemTra (TieuDe, LoaiBaiKiemTra, MaMonHoc, CoXaoTron, TongSoCauHoi) VALUES 
(N'Củng cố Toán 1','CungCo',1,1,10),
(N'Củng cố Tiếng Việt 1','CungCo',2,1,10),
(N'Ôn tập hỗn hợp - cơ bản 1','OnTapCoBan',NULL,1,10),
(N'Ôn tập hỗn hợp - cơ bản 2','OnTapCoBan',NULL,1,10),
(N'Ôn tập hỗn hợp - trung bình 1','OnTapTrungBinh',NULL,1,10),
(N'Ôn tập hỗn hợp - trung bình 2','OnTapTrungBinh',NULL,1,10),
(N'Ôn tập hỗn hợp - nâng cao 1','OnTapNangCao',NULL,1,10),
(N'Ôn tập hỗn hợp - nâng cao 2','OnTapNangCao',NULL,1,10),
('Thử thách chọn đáp án A/B/C/D 1','ThuThach',NULL,1,10);
GO

-- Câu hỏi
CREATE TABLE CauHoi (
    MaCauHoi INT IDENTITY(1,1) PRIMARY KEY,
    NoiDung NVARCHAR(MAX) NOT NULL,
    MaMonHoc TINYINT NULL REFERENCES MonHoc(MaMonHoc), -- NULL nếu hỗn hợp
    GiaiThich NVARCHAR(MAX) NULL
);
-- Insert 60 câu hỏi mẫu (10/đề x 6 đề chính, chia Toán/TV/Hỗn hợp)
-- Toán (20 câu): Phép tính cơ bản
INSERT INTO CauHoi (MaMonHoc, NoiDung, GiaiThich) VALUES
(1, N'2 + 3 = ?', N'5'),
(1, N'5 - 2 = ?', N'3'),
(1, N'4 * 2 = ?', N'8'),
(1, N'10 / 2 = ?', N'5'),
(1, N'1 + 4 = ?', N'5'),
(1, N'6 - 1 = ?', N'5'),
(1, N'3 * 3 = ?', N'9'),
(1, N'8 / 4 = ?', N'2'),
(1, N'7 + 1 = ?', N'8'),
(1, N'9 - 4 = ?', N'5'),
(1, N'2 * 5 = ?', N'10'),
(1, N'12 / 3 = ?', N'4'),
(1, N'3 + 6 = ?', N'9'),
(1, N'8 - 3 = ?', N'5'),
(1, N'4 * 4 = ?', N'16'),
(1, N'15 / 5 = ?', N'3'),
(1, N'5 + 5 = ?', N'10'),
(1, N'10 - 5 = ?', N'5'),
(1, N'6 * 2 = ?', N'12'),
(1, N'18 / 6 = ?', N'3');
-- Tiếng Việt (20 câu): Chọn từ đúng
INSERT INTO CauHoi (MaMonHoc, NoiDung, GiaiThich) VALUES
(2, N'Chữ cái thứ 1 là?', N'A'),
(2, N'Chữ cái thứ 2 là?', N'B'),
(2, N'Từ ghép: Nhà ___', N'Cửa'),
(2, N'Cây ___', N'Cối'),
(2, N'Con mèo ___', N'Chạy'),
(2, N'Con chó ___', N'Ăn'),
(2, N'Sách ___', N'Vở'),
(2, N'Bàn ___', N'Ghế'),
(2, N'Mưa ___', N'Rơi'),
(2, N'Nắng ___', N'Nóng'),
(2, N'Con cá ___', N'Bơi'),
(2, N'Con chim ___', N'Bay'),
(2, N'Quả táo ___', N'Đỏ'),
(2, N'Quả chuối ___', N'Vàng'),
(2, N'Sông ___', N'Nước'),
(2, N'Núi ___', N'Cao'),
(2, N'Biển ___', N'Xanh'),
(2, N'Rừng ___', N'Xanh'),
(2, N'Trời ___', N'Xanh'),
(2, N'Đất ___', N'Nâu');
-- Hỗn hợp (20 câu): Kết hợp Toán/TV
INSERT INTO CauHoi (NoiDung, GiaiThich) VALUES
(N'2 + 2 = ? (Toán)', N'4'),
(N'A là chữ cái đầu?', N'Có'),
(N'3 * 3 = ? (Toán)', N'9'),
(N'Nhà cửa là từ ghép?', N'Có'),
(N'5 - 1 = ? (Toán)', N'4'),
(N'Con mèo chạy (Câu đơn)?', N'Có'),
(N'4 + 4 = ? (Toán)', N'8'),
(N'Chữ cái thứ 3?', N'C'),
(N'10 / 2 = ? (Toán)', N'5'),
(N'Cây cối là từ ghép?', N'Có'),
(N'6 + 2 = ? (Toán)', N'8'),
(N'Con chó ăn (Câu đơn)?', N'Có'),
(N'7 - 3 = ? (Toán)', N'4'),
(N'Sách vở là từ ghép?', N'Có'),
(N'8 * 1 = ? (Toán)', N'8'),
(N'Bàn ghế là từ ghép?', N'Có'),
(N'9 / 3 = ? (Toán)', N'3'),
(N'Mưa rơi (Câu đơn)?', N'Có'),
(N'1 + 9 = ? (Toán)', N'10'),
(N'Nắng nóng (Từ ghép)?', N'Có');
GO

-- Lựa chọn (A,B,C,D)
CREATE TABLE LuaChon (
    MaLuaChon INT IDENTITY(1,1) PRIMARY KEY,
    MaCauHoi INT NOT NULL REFERENCES CauHoi(MaCauHoi) ON DELETE CASCADE,
    NhanLuaChon CHAR(1) NOT NULL, -- 'A','B','C','D'
    NoiDung NVARCHAR(MAX) NOT NULL,
    LaDung BIT NOT NULL DEFAULT 0
);
-- Insert 4 lựa chọn cho mỗi câu hỏi (240 records: A đúng, B/C/D sai)
DECLARE @cau_id INT = 1;
WHILE @cau_id <= 60
BEGIN
    INSERT INTO LuaChon (MaCauHoi, NhanLuaChon, NoiDung, LaDung) VALUES
    (@cau_id, 'A', N'Đáp án đúng: ' + CAST(@cau_id AS NVARCHAR(10)), 1),  -- A đúng
    (@cau_id, 'B', N'Đáp án sai B', 0),
    (@cau_id, 'C', N'Đáp án sai C', 0),
    (@cau_id, 'D', N'Đáp án sai D', 0);
    SET @cau_id = @cau_id + 1;
END
GO

-- Liên kết Bài kiểm tra <-> Câu hỏi (có thể lộn xộn giữa Toán & TV)
CREATE TABLE BaiKiemTraCauHoi (
    MaBaiKiemTra INT NOT NULL REFERENCES BaiKiemTra(MaBaiKiemTra) ON DELETE CASCADE,
    MaCauHoi INT NOT NULL REFERENCES CauHoi(MaCauHoi),
    ThuTu TINYINT NOT NULL,
    CONSTRAINT PK_BaiKiemTraCauHoi PRIMARY KEY (MaBaiKiemTra, MaCauHoi)
);
-- Gán 10 câu cho mỗi đề (9 đề x 10 = 90 links, cycle qua câu hỏi 1-60)
DECLARE @bai_id INT = 1, @thu_tu TINYINT = 1, @cau_counter INT = 1;
WHILE @bai_id <= 9
BEGIN
    SET @thu_tu = 1;
    WHILE @thu_tu <= 10
    BEGIN
        INSERT INTO BaiKiemTraCauHoi (MaBaiKiemTra, MaCauHoi, ThuTu) VALUES (@bai_id, @cau_counter, @thu_tu);
        SET @cau_counter = @cau_counter + 1;
        IF @cau_counter > 60 SET @cau_counter = 1;  -- Cycle
        SET @thu_tu = @thu_tu + 1;
    END
    SET @bai_id = @bai_id + 1;
END
GO

-- Tiến độ lý thuyết của người dùng / hoàn thành lý thuyết
CREATE TABLE TienDoLyThuyetNguoiDung (
    MaNguoiDung INT NOT NULL REFERENCES NguoiDung(MaNguoiDung),
    MaLyThuyet INT NOT NULL REFERENCES LyThuyet(MaLyThuyet),
    NgayHoanThanh DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME(),
    DiemThuong INT NOT NULL DEFAULT 0,
    CONSTRAINT PK_TienDoLyThuyetNguoiDung PRIMARY KEY (MaNguoiDung, MaLyThuyet)
);
-- Mẫu: User 1 (Thọ Khang) hoàn thành 2 lý thuyết Toán
INSERT INTO TienDoLyThuyetNguoiDung (MaNguoiDung, MaLyThuyet, DiemThuong) VALUES (1, 1, 10), (1, 2, 10);
GO

-- Lần thử bài kiểm tra của người dùng (lưu lịch sử làm bài, điểm)
CREATE TABLE LanThuBaiKiemTraNguoiDung (
    MaLanThu INT IDENTITY(1,1) PRIMARY KEY,
    MaNguoiDung INT NOT NULL REFERENCES NguoiDung(MaNguoiDung),
    MaBaiKiemTra INT NOT NULL REFERENCES BaiKiemTra(MaBaiKiemTra),
    Diem INT NOT NULL,
    DiemToiDa INT NOT NULL,
    NgayHoanThanh DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME(),
    CONSTRAINT CK_LanThu_Diem CHECK (Diem >= 0),
    CONSTRAINT CK_LanThu_DiemToiDa CHECK (DiemToiDa > 0),
    CONSTRAINT CK_LanThu_DiemLeDiemToiDa CHECK (Diem <= DiemToiDa)
);
-- Mẫu: User 1 làm 1 đề củng cố, điểm 40/50
INSERT INTO LanThuBaiKiemTraNguoiDung (MaNguoiDung, MaBaiKiemTra, Diem, DiemToiDa) VALUES (1, 1, 40, 50);
GO

-- Đáp án từng câu cho mỗi lần thử
CREATE TABLE DapAnCauHoiNguoiDung (
    MaLanThu INT NOT NULL REFERENCES LanThuBaiKiemTraNguoiDung(MaLanThu) ON DELETE CASCADE,
    MaCauHoi INT NOT NULL REFERENCES CauHoi(MaCauHoi),
    MaLuaChonChon INT NULL REFERENCES LuaChon(MaLuaChon),
    LaDung BIT NOT NULL,
    CONSTRAINT PK_DapAnCauHoiNguoiDung PRIMARY KEY (MaLanThu, MaCauHoi)
);
-- Mẫu: 10 đáp án cho attempt 1 (5 đúng, 5 sai)
DECLARE @lan_thu INT = 1, @cau_id INT = 1;
WHILE @cau_id <= 10
BEGIN
    DECLARE @la_dung BIT = CASE WHEN @cau_id % 2 = 1 THEN 1 ELSE 0 END;
    DECLARE @lua_chon INT = CASE WHEN @la_dung = 1 THEN ( (@cau_id - 1) * 4 + 1 ) ELSE ( (@cau_id - 1) * 4 + 2 ) END;  -- Giả sử ID lựa chọn A đúng
    INSERT INTO DapAnCauHoiNguoiDung (MaLanThu, MaCauHoi, MaLuaChonChon, LaDung) VALUES (@lan_thu, @cau_id, @lua_chon, @la_dung);
    SET @cau_id = @cau_id + 1;
END
GO

-- Trò chơi
CREATE TABLE TroChoi (
    MaTroChoi INT IDENTITY(1,1) PRIMARY KEY,
    TenTroChoi NVARCHAR(150) NOT NULL, -- 'liên hoàn tính toán' ...
    MoTa NVARCHAR(MAX) NULL,
    DiemThanhCong INT NOT NULL DEFAULT 80
);
INSERT INTO TroChoi (TenTroChoi, MoTa, DiemThanhCong)
VALUES
(N'Liên hoàn tính toán',N'Chuỗi phép tính nhỏ, đúng hết được điểm',80),
(N'Hoàn thiện câu từ',N'Điền vào chỗ trống',80),
(N'Trùm tính toán',N'Chuỗi bài nâng cao',80);
GO

-- Lần thử trò chơi (lưu khi chơi, nếu đúng hết thì full points)
CREATE TABLE LanThuTroChoi (
    MaLanThuTroChoi INT IDENTITY(1,1) PRIMARY KEY,
    MaNguoiDung INT NOT NULL REFERENCES NguoiDung(MaNguoiDung),
    MaTroChoi INT NOT NULL REFERENCES TroChoi(MaTroChoi),
    Diem INT NOT NULL,
    ThanhCong BIT NOT NULL,
    NgayHoanThanh DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()
);
-- Mẫu: User 1 chơi game 1 thành công
INSERT INTO LanThuTroChoi (MaNguoiDung, MaTroChoi, Diem, ThanhCong) VALUES (1, 1, 80, 1);
GO

-- Thử thách
CREATE TABLE ThuThach (
    MaThuThach INT IDENTITY(1,1) PRIMARY KEY,
    TieuDe NVARCHAR(200) NOT NULL,
    MoTa NVARCHAR(MAX) NULL,
    Diem INT NOT NULL DEFAULT 80
);
INSERT INTO ThuThach (TieuDe, MoTa, Diem)
VALUES
('Thử thách 1','Đề chọn đáp án A/B/C/D',80),
('Thử thách 2','Đề chọn đáp án A/B/C/D',80);
GO

CREATE TABLE LanThuThuThach (
    MaLanThuThuThach INT IDENTITY(1,1) PRIMARY KEY,
    MaNguoiDung INT NOT NULL REFERENCES NguoiDung(MaNguoiDung),
    MaThuThach INT NOT NULL REFERENCES ThuThach(MaThuThach),
    Diem INT NOT NULL,
    NgayHoanThanh DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()
);
-- Mẫu: User 1 hoàn thành thử thách 1
INSERT INTO LanThuThuThach (MaNguoiDung, MaThuThach, Diem) VALUES (1, 1, 80);
GO

-- Thông báo (cài đặt thông báo & lưu thông báo)
CREATE TABLE ThongBao (
    MaThongBao INT IDENTITY(1,1) PRIMARY KEY,
    MaNguoiDung INT NULL REFERENCES NguoiDung(MaNguoiDung),
    NoiDung NVARCHAR(500) NOT NULL,
    DaDoc BIT DEFAULT 0,
    NgayTao DATETIME2 DEFAULT SYSUTCDATETIME()
);
-- Mẫu: 2 thông báo
INSERT INTO ThongBao (MaNguoiDung, NoiDung) VALUES (1, N'Chào mừng bạn đến với ứng dụng!'), (2, N'Thông báo cho admin');
GO

-- LichHenGio: lưu lịch hẹn giờ mở ứng dụng (cần service đọc bảng này)
CREATE TABLE LichHenGio (
    MaLich INT IDENTITY(1,1) PRIMARY KEY,
    MaNguoiDung INT NULL REFERENCES NguoiDung(MaNguoiDung),
    ThoiGianHen TIME NOT NULL, -- giờ trong ngày
    LapLai NVARCHAR(50) NULL, -- 'HangNgay','MotLan','NgayTrongTuan',...
    NgayChayTiepTheo DATE NULL,
    DaKichHoat BIT DEFAULT 1,
    NgayTao DATETIME2 DEFAULT SYSUTCDATETIME()
);
-- Mẫu: Lịch nhắc cho user 1
INSERT INTO LichHenGio (MaNguoiDung, ThoiGianHen, LapLai) VALUES (1, '08:00:00', N'HangNgay');
GO

-- HoatDongHangNgay: lưu hành vi trong ngày (đã đăng nhập, tổng điểm trong ngày)
CREATE TABLE HoatDongHangNgay (
    MaNguoiDung INT NOT NULL REFERENCES NguoiDung(MaNguoiDung),
    NgayHoatDong DATE NOT NULL,
    DaDangNhap BIT DEFAULT 0,
    DiemKiemDuoc INT DEFAULT 0,
    CONSTRAINT PK_HoatDongHangNgay PRIMARY KEY (MaNguoiDung, NgayHoatDong),
    CONSTRAINT CK_HoatDong_DiemKiemDuoc CHECK (DiemKiemDuoc >= 0)
);
-- Mẫu: Hoạt động hôm nay cho user 1
INSERT INTO HoatDongHangNgay (MaNguoiDung, NgayHoatDong, DaDangNhap, DiemKiemDuoc) VALUES (1, CAST(SYSDATETIME() AS DATE), 1, 20);
GO

-- View xếp hạng (xếp hạng theo điểm và theo số lần trực tuyến)
CREATE VIEW vw_XepHangNguoiDung AS
SELECT
    nd.MaNguoiDung,
    nd.TenDangNhap,
    nd.TongDiem,
    nd.SoLanTrucTuyen,
    RANK() OVER (ORDER BY nd.TongDiem DESC, nd.SoLanTrucTuyen DESC) AS XepHangTheoDiem
FROM NguoiDung nd;
GO

-- Chỉ mục hữu ích
CREATE INDEX IX_LanThuBaiKiemTraNguoiDung_MaNguoiDung ON LanThuBaiKiemTraNguoiDung(MaNguoiDung);
CREATE INDEX IX_TienDoLyThuyetNguoiDung_MaNguoiDung ON TienDoLyThuyetNguoiDung(MaNguoiDung);
CREATE INDEX IX_HoatDongHangNgay_Ngay ON HoatDongHangNgay(NgayHoatDong);
GO

/******************************************
 Thủ tục lưu trữ / kiểu hỗ trợ
*******************************************/

-- TVP cho đáp án khi submit bài kiểm tra (MaCauHoi, MaLuaChonChon)
IF TYPE_ID(N'dbo.KieuBangDapAn') IS NOT NULL
    DROP TYPE dbo.KieuBangDapAn;
GO
CREATE TYPE dbo.KieuBangDapAn AS TABLE(
    MaCauHoi INT NOT NULL,
    MaLuaChonChon INT NULL
);
GO

-- Thủ tục: HoanThanhLyThuyet (thưởng 10 điểm lần đầu; học lại không cộng)
IF OBJECT_ID('dbo.usp_HoanThanhLyThuyet','P') IS NOT NULL
    DROP PROCEDURE dbo.usp_HoanThanhLyThuyet;
GO
CREATE PROCEDURE dbo.usp_HoanThanhLyThuyet
    @MaNguoiDung INT,
    @MaLyThuyet INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;

        -- Kiểm tra đã hoàn thành chưa
        IF EXISTS (SELECT 1 FROM TienDoLyThuyetNguoiDung WHERE MaNguoiDung = @MaNguoiDung AND MaLyThuyet = @MaLyThuyet)
        BEGIN
            -- đã học rồi -> không cộng điểm
            SELECT CAST(0 AS INT) AS DiemThem, 'DaHoanThanh' AS TrangThai;
            ROLLBACK TRAN;
            RETURN;
        END

        DECLARE @Diem INT = 10;

        INSERT INTO TienDoLyThuyetNguoiDung (MaNguoiDung, MaLyThuyet, DiemThuong)
        VALUES (@MaNguoiDung, @MaLyThuyet, @Diem);

        -- Cộng tổng điểm người dùng
        UPDATE NguoiDung SET TongDiem = ISNULL(TongDiem,0) + @Diem WHERE MaNguoiDung = @MaNguoiDung;

        -- Cập nhật HoatDongHangNgay (sử dụng múi giờ Việt Nam)
        MERGE HoatDongHangNgay AS hd
        USING (SELECT @MaNguoiDung AS MaNguoiDung, CAST(SYSDATETIME() AT TIME ZONE 'SE Asia Standard Time' AS date) AS NgayHoatDong) AS src
        ON (hd.MaNguoiDung = src.MaNguoiDung AND hd.NgayHoatDong = src.NgayHoatDong)
        WHEN MATCHED THEN
            UPDATE SET hd.DiemKiemDuoc = hd.DiemKiemDuoc + @Diem, hd.DaDangNhap = 1
        WHEN NOT MATCHED THEN
            INSERT (MaNguoiDung, NgayHoatDong, DaDangNhap, DiemKiemDuoc)
            VALUES (src.MaNguoiDung, src.NgayHoatDong, 1, @Diem);

        -- Tăng SoLanTrucTuyen nếu chưa tăng hôm nay (giả sử gọi quy trình đăng nhập khác sẽ tăng)
        UPDATE NguoiDung SET SoLanTrucTuyen = SoLanTrucTuyen WHERE MaNguoiDung = @MaNguoiDung;

        COMMIT TRAN;
        SELECT @Diem AS DiemThem, 'HoanThanh' AS TrangThai;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRAN;
        THROW;
    END CATCH
END;
GO

-- Thủ tục: GuiBaiKiemTra (sửa để hỗ trợ làm lại +điểm câu sai trước)
IF OBJECT_ID('dbo.usp_GuiBaiKiemTra','P') IS NOT NULL
    DROP PROCEDURE dbo.usp_GuiBaiKiemTra;
GO
CREATE PROCEDURE dbo.usp_GuiBaiKiemTra
    @MaNguoiDung INT,
    @MaBaiKiemTra INT,
    @DapAn dbo.KieuBangDapAn READONLY
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;

        DECLARE @LoaiBaiKiemTra NVARCHAR(50);
        SELECT @LoaiBaiKiemTra = LoaiBaiKiemTra FROM BaiKiemTra WHERE MaBaiKiemTra = @MaBaiKiemTra;

        IF @LoaiBaiKiemTra IS NULL
        BEGIN
            THROW 51000, 'Bài kiểm tra không tồn tại', 1;
        END

        -- Xác định điểm mỗi câu & max (riêng cho ThuThach)
        DECLARE @DiemMoiCau INT = 5;
        DECLARE @DiemToiDa INT = 50;
        IF @LoaiBaiKiemTra = 'ThuThach'
        BEGIN
            SET @DiemToiDa = 80;
            SET @DiemMoiCau = 8; -- 80 / 10 câu
        END
        ELSE IF @LoaiBaiKiemTra = 'CungCo' OR @LoaiBaiKiemTra = 'OnTapCoBan'
        BEGIN
            SET @DiemMoiCau = 5; 
            SET @DiemToiDa = 50;
        END
        ELSE IF @LoaiBaiKiemTra = 'OnTapTrungBinh'
        BEGIN
            SET @DiemMoiCau = 8; 
            SET @DiemToiDa = 80;
        END
        ELSE IF @LoaiBaiKiemTra = 'OnTapNangCao'
        BEGIN
            SET @DiemMoiCau = 10; 
            SET @DiemToiDa = 100;
        END

        -- Tính số câu đúng lần này
        DECLARE @SoCauDung INT = 0;
        DECLARE @BangKetQuaTam TABLE (MaCauHoi INT, MaLuaChonChon INT, LaDung BIT);

        INSERT INTO @BangKetQuaTam (MaCauHoi, MaLuaChonChon, LaDung)
        SELECT da.MaCauHoi, da.MaLuaChonChon,
               CASE WHEN lc.LaDung = 1 THEN 1 ELSE 0 END
        FROM @DapAn da
        LEFT JOIN LuaChon lc ON lc.MaLuaChon = da.MaLuaChonChon;

        SELECT @SoCauDung = ISNULL(SUM(CASE WHEN LaDung = 1 THEN 1 ELSE 0 END), 0) FROM @BangKetQuaTam;

        DECLARE @DiemLanNay INT = @SoCauDung * @DiemMoiCau;

        -- Insert attempt mới (luôn insert, giữ lịch sử)
        INSERT INTO LanThuBaiKiemTraNguoiDung (MaNguoiDung, MaBaiKiemTra, Diem, DiemToiDa)
        VALUES (@MaNguoiDung, @MaBaiKiemTra, @DiemLanNay, @DiemToiDa);

        DECLARE @MaLanThu INT = SCOPE_IDENTITY();

        -- Insert đáp án từng câu cho attempt mới
        INSERT INTO DapAnCauHoiNguoiDung (MaLanThu, MaCauHoi, MaLuaChonChon, LaDung)
        SELECT @MaLanThu, MaCauHoi, MaLuaChonChon, LaDung FROM @BangKetQuaTam;

        -- Xử lý điểm thưởng
        DECLARE @DiemThem INT = 0;
        IF NOT EXISTS (SELECT 1 FROM LanThuBaiKiemTraNguoiDung WHERE MaNguoiDung = @MaNguoiDung AND MaBaiKiemTra = @MaBaiKiemTra AND MaLanThu <> @MaLanThu)
        BEGIN
            -- Lần đầu: +toàn bộ DiemLanNay
            SET @DiemThem = @DiemLanNay;
        END
        ELSE
        BEGIN
            -- Làm lại: Tìm attempt trước gần nhất, so sánh câu sai trước mà giờ đúng
            DECLARE @MaLanThuTruoc INT = (SELECT TOP 1 MaLanThu FROM LanThuBaiKiemTraNguoiDung WHERE MaNguoiDung = @MaNguoiDung AND MaBaiKiemTra = @MaBaiKiemTra AND MaLanThu <> @MaLanThu ORDER BY NgayHoanThanh DESC);

            IF @MaLanThuTruoc IS NOT NULL
            BEGIN
                DECLARE @SoCauThemDung INT = 0;

                SELECT @SoCauThemDung = COUNT(*)
                FROM @BangKetQuaTam nay
                INNER JOIN DapAnCauHoiNguoiDung truoc ON nay.MaCauHoi = truoc.MaCauHoi AND truoc.MaLanThu = @MaLanThuTruoc
                WHERE nay.LaDung = 1 AND truoc.LaDung = 0;  -- Sai trước, đúng nay

                SET @DiemThem = @SoCauThemDung * @DiemMoiCau;
            END
        END

        -- +Điểm thêm vào TongDiem và HoatDongHangNgay nếu có
        IF @DiemThem > 0
        BEGIN
            UPDATE NguoiDung SET TongDiem = ISNULL(TongDiem,0) + @DiemThem WHERE MaNguoiDung = @MaNguoiDung;

            MERGE HoatDongHangNgay AS hd
            USING (SELECT @MaNguoiDung AS MaNguoiDung, CAST(SYSDATETIME() AT TIME ZONE 'SE Asia Standard Time' AS DATE) AS NgayHoatDong) AS src
            ON (hd.MaNguoiDung = src.MaNguoiDung AND hd.NgayHoatDong = src.NgayHoatDong)
            WHEN MATCHED THEN
                UPDATE SET hd.DiemKiemDuoc = hd.DiemKiemDuoc + @DiemThem, hd.DaDangNhap = 1
            WHEN NOT MATCHED THEN
                INSERT (MaNguoiDung, NgayHoatDong, DaDangNhap, DiemKiemDuoc)
                VALUES (src.MaNguoiDung, src.NgayHoatDong, 1, @DiemThem);
        END

        COMMIT TRAN;

        SELECT @MaLanThu AS MaLanThu, @DiemLanNay AS Diem, @DiemToiDa AS DiemToiDa, @SoCauDung AS SoCauDung, @DiemThem AS DiemThem;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRAN;
        THROW;
    END CATCH
END;
GO

-- Thủ tục: GhiNhanLanThuTroChoi
IF OBJECT_ID('dbo.usp_GhiNhanLanThuTroChoi','P') IS NOT NULL
    DROP PROCEDURE dbo.usp_GhiNhanLanThuTroChoi;
GO
CREATE PROCEDURE dbo.usp_GhiNhanLanThuTroChoi
    @MaNguoiDung INT,
    @MaTroChoi INT,
    @Diem INT,
    @ThanhCong BIT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;
        INSERT INTO LanThuTroChoi (MaNguoiDung, MaTroChoi, Diem, ThanhCong)
        VALUES (@MaNguoiDung, @MaTroChoi, @Diem, @ThanhCong);

        IF @ThanhCong = 1
        BEGIN
            -- thưởng điểm (full điểm từ bảng TroChoi)
            DECLARE @DiemT INT = (SELECT DiemThanhCong FROM TroChoi WHERE MaTroChoi = @MaTroChoi);
            UPDATE NguoiDung SET TongDiem = ISNULL(TongDiem,0) + ISNULL(@DiemT,0) WHERE MaNguoiDung = @MaNguoiDung;

            MERGE HoatDongHangNgay AS hd
            USING (SELECT @MaNguoiDung AS MaNguoiDung, CAST(SYSDATETIME() AT TIME ZONE 'SE Asia Standard Time' AS DATE) AS NgayHoatDong) AS src
            ON (hd.MaNguoiDung = src.MaNguoiDung AND hd.NgayHoatDong = src.NgayHoatDong)
            WHEN MATCHED THEN
                UPDATE SET hd.DiemKiemDuoc = hd.DiemKiemDuoc + ISNULL(@DiemT,0), hd.DaDangNhap = 1
            WHEN NOT MATCHED THEN
                INSERT (MaNguoiDung, NgayHoatDong, DaDangNhap, DiemKiemDuoc)
                VALUES (src.MaNguoiDung, src.NgayHoatDong, 1, ISNULL(@DiemT,0));
        END

        COMMIT TRAN;
        SELECT SCOPE_IDENTITY() AS MaLanThuTroChoi;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRAN;
        THROW;
    END CATCH
END;
GO

-- Thủ tục: GuiLanThuThuThach (80 điểm nếu thành công)
IF OBJECT_ID('dbo.usp_GuiLanThuThuThach','P') IS NOT NULL
    DROP PROCEDURE dbo.usp_GuiLanThuThuThach;
GO
CREATE PROCEDURE dbo.usp_GuiLanThuThuThach
    @MaNguoiDung INT,
    @MaThuThach INT,
    @Diem INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;
        INSERT INTO LanThuThuThach (MaNguoiDung, MaThuThach, Diem)
        VALUES (@MaNguoiDung, @MaThuThach, @Diem);

        -- thưởng điểm: nếu diem bằng full possible? Yêu cầu: "làm được 80 điểm" -> giả sử Diem chỉ earned, nhưng thưởng Diem.
        UPDATE NguoiDung SET TongDiem = ISNULL(TongDiem,0) + @Diem WHERE MaNguoiDung = @MaNguoiDung;

        MERGE HoatDongHangNgay AS hd
        USING (SELECT @MaNguoiDung AS MaNguoiDung, CAST(SYSDATETIME() AT TIME ZONE 'SE Asia Standard Time' AS DATE) AS NgayHoatDong) AS src
        ON (hd.MaNguoiDung = src.MaNguoiDung AND hd.NgayHoatDong = src.NgayHoatDong)
        WHEN MATCHED THEN
            UPDATE SET hd.DiemKiemDuoc = hd.DiemKiemDuoc + @Diem, hd.DaDangNhap = 1
        WHEN NOT MATCHED THEN
            INSERT (MaNguoiDung, NgayHoatDong, DaDangNhap, DiemKiemDuoc)
            VALUES (src.MaNguoiDung, src.NgayHoatDong, 1, @Diem);

        COMMIT TRAN;
        SELECT SCOPE_IDENTITY() AS MaLanThuThuThach;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0
            ROLLBACK TRAN;
        THROW;
    END CATCH
END;
GO

-- Thủ tục: LayTopBangXepHang theo điểm
IF OBJECT_ID('dbo.usp_LayTopBangXepHang','P') IS NOT NULL
    DROP PROCEDURE dbo.usp_LayTopBangXepHang;
GO
CREATE PROCEDURE dbo.usp_LayTopBangXepHang
    @TopN INT = 10
AS
BEGIN
    SET NOCOUNT ON;
    SELECT TOP (@TopN)
        nd.MaNguoiDung, nd.TenDangNhap, nd.TongDiem, nd.SoLanTrucTuyen
    FROM NguoiDung nd
    ORDER BY nd.TongDiem DESC, nd.SoLanTrucTuyen DESC;
END;
GO

-- Trigger hoặc job để đánh dấu "DaDangNhap" khi người dùng đăng nhập nên ở lớp ứng dụng.
-- Chúng tôi cung cấp ví dụ trigger tăng SoLanTrucTuyen khi cập nhật LanDangNhapCuoi (TÙY CHỌN - khuyến nghị di chuyển sang app layer)
IF OBJECT_ID('TR_NguoiDung_CapNhatLanDangNhapCuoi','TR') IS NOT NULL
    DROP TRIGGER TR_NguoiDung_CapNhatLanDangNhapCuoi;
GO
CREATE TRIGGER TR_NguoiDung_CapNhatLanDangNhapCuoi
ON NguoiDung
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    IF UPDATE(LanDangNhapCuoi)
    BEGIN
        UPDATE nd
        SET SoLanTrucTuyen = nd.SoLanTrucTuyen + 1
        FROM NguoiDung nd
        INNER JOIN inserted i ON nd.MaNguoiDung = i.MaNguoiDung;
    END
END;
GO

-- Stored Procedure: DangNhap (cập nhật login và kiểm tra học xong môn để set DaDangNhap)
IF OBJECT_ID('dbo.usp_DangNhap','P') IS NOT NULL
    DROP PROCEDURE dbo.usp_DangNhap;
GO
CREATE PROCEDURE dbo.usp_DangNhap
    @MaNguoiDung INT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        BEGIN TRAN;

        -- Cập nhật LastLogin
        UPDATE NguoiDung 
        SET LanDangNhapCuoi = SYSDATETIME() AT TIME ZONE 'SE Asia Standard Time'
        WHERE MaNguoiDung = @MaNguoiDung;

        -- Kiểm tra nếu đã học xong ít nhất 1 môn hôm nay (ví dụ: có TienDoLyThuyetNguoiDung hoặc LanThuBaiKiemTraNguoiDung trong ngày)
        DECLARE @NgayHomNay DATE = CAST(SYSDATETIME() AT TIME ZONE 'SE Asia Standard Time' AS DATE);
        DECLARE @DaHocMon BIT = 0;
        IF EXISTS (
            SELECT 1 FROM TienDoLyThuyetNguoiDung t 
            INNER JOIN LyThuyet l ON t.MaLyThuyet = l.MaLyThuyet
            WHERE t.MaNguoiDung = @MaNguoiDung AND CAST(t.NgayHoanThanh AS DATE) = @NgayHomNay
            UNION
            SELECT 1 FROM LanThuBaiKiemTraNguoiDung lt 
            INNER JOIN BaiKiemTra b ON lt.MaBaiKiemTra = b.MaBaiKiemTra
            WHERE lt.MaNguoiDung = @MaNguoiDung AND CAST(lt.NgayHoanThanh AS DATE) = @NgayHomNay AND lt.Diem > 0  -- Có điểm >0 nghĩa là học xong
        )
        BEGIN
            SET @DaHocMon = 1;
        END

        -- Nếu đã học xong môn hôm nay, set DaDangNhap=1 (không thưởng điểm)
        IF @DaHocMon = 1
        BEGIN
            MERGE HoatDongHangNgay AS hd
            USING (SELECT @MaNguoiDung AS MaNguoiDung, @NgayHomNay AS NgayHoatDong) AS src
            ON (hd.MaNguoiDung = src.MaNguoiDung AND hd.NgayHoatDong = src.NgayHoatDong)
            WHEN MATCHED THEN
                UPDATE SET hd.DaDangNhap = 1
            WHEN NOT MATCHED THEN
                INSERT (MaNguoiDung, NgayHoatDong, DaDangNhap, DiemKiemDuoc)
                VALUES (src.MaNguoiDung, src.NgayHoatDong, 1, 0);  -- Diem=0 vì không thưởng lúc login
        END

        COMMIT TRAN;
        SELECT @DaHocMon AS DaHocMonHomNay;
    END TRY
    BEGIN CATCH
        IF @@TRANCOUNT > 0 ROLLBACK TRAN;
        THROW;
    END CATCH
END;
GO

-- Kiểm tra dữ liệu đã insert (counts cho từng bảng)
SELECT 'VaiTro' AS Bang, COUNT(*) AS SoLuong FROM VaiTro
UNION ALL SELECT 'NguoiDung', COUNT(*) FROM NguoiDung
UNION ALL SELECT 'MonHoc', COUNT(*) FROM MonHoc
UNION ALL SELECT 'LyThuyet', COUNT(*) FROM LyThuyet
UNION ALL SELECT 'BaiKiemTra', COUNT(*) FROM BaiKiemTra
UNION ALL SELECT 'CauHoi', COUNT(*) FROM CauHoi
UNION ALL SELECT 'LuaChon', COUNT(*) FROM LuaChon
UNION ALL SELECT 'BaiKiemTraCauHoi', COUNT(*) FROM BaiKiemTraCauHoi
UNION ALL SELECT 'TienDoLyThuyetNguoiDung', COUNT(*) FROM TienDoLyThuyetNguoiDung
UNION ALL SELECT 'LanThuBaiKiemTraNguoiDung', COUNT(*) FROM LanThuBaiKiemTraNguoiDung
UNION ALL SELECT 'DapAnCauHoiNguoiDung', COUNT(*) FROM DapAnCauHoiNguoiDung
UNION ALL SELECT 'TroChoi', COUNT(*) FROM TroChoi
UNION ALL SELECT 'LanThuTroChoi', COUNT(*) FROM LanThuTroChoi
UNION ALL SELECT 'ThuThach', COUNT(*) FROM ThuThach
UNION ALL SELECT 'LanThuThuThach', COUNT(*) FROM LanThuThuThach
UNION ALL SELECT 'ThongBao', COUNT(*) FROM ThongBao
UNION ALL SELECT 'LichHenGio', COUNT(*) FROM LichHenGio
UNION ALL SELECT 'HoatDongHangNgay', COUNT(*) FROM HoatDongHangNgay;
GO
select * from NguoiDung
SELECT TABLE_SCHEMA, TABLE_NAME 
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_NAME = 'NguoiDung';
SELECT name, SCHEMA_NAME(schema_id) AS schema_name, create_date
FROM sys.tables
WHERE name = 'NguoiDung'
ORDER BY create_date DESC;
SELECT * FROM nguoi_dung;
DROP TABLE nguoi_dung;

select * from BaiKiemTra
select * from BaiKiemTraCauHoi