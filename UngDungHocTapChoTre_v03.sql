USE master;
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'UngDungHocTapChoTre')
BEGIN
    ALTER DATABASE UngDungHocTapChoTre SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE UngDungHocTapChoTre;
END;
GO
-- Tạo database
CREATE DATABASE UngDungHocTapChoTre
GO
USE UngDungHocTapChoTre
GO

-- Tạo tất cả các bảng trước

-- Vai trò (Admin/Người dùng) (bảng tùy chọn)
CREATE TABLE VaiTro (
    MaVaiTro TINYINT PRIMARY KEY,
    TenVaiTro NVARCHAR(50) NOT NULL UNIQUE
);
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
GO

-- Môn học
CREATE TABLE MonHoc (
    MaMonHoc TINYINT PRIMARY KEY,
    TenMonHoc NVARCHAR(100) NOT NULL UNIQUE -- 'Toán', 'Tiếng Việt'
);
GO

-- Lý thuyết (lý thuyết cơ bản)
CREATE TABLE LyThuyet (
    MaLyThuyet INT IDENTITY(1,1) PRIMARY KEY,
    MaMonHoc TINYINT NOT NULL REFERENCES MonHoc(MaMonHoc),
    TieuDe NVARCHAR(200) NOT NULL,
    NoiDung NVARCHAR(MAX) NULL,
    NgayTao DATETIME2 DEFAULT SYSUTCDATETIME()
);
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
GO

-- Câu hỏi
CREATE TABLE CauHoi (
    MaCauHoi INT IDENTITY(1,1) PRIMARY KEY,
    NoiDung NVARCHAR(MAX) NOT NULL,
    MaMonHoc TINYINT NULL REFERENCES MonHoc(MaMonHoc), -- NULL nếu hỗn hợp
    GiaiThich NVARCHAR(MAX) NULL
);
GO

-- Lựa chọn (A,B,C,D)
CREATE TABLE LuaChon (
    MaLuaChon INT IDENTITY(1,1) PRIMARY KEY,
    MaCauHoi INT NOT NULL REFERENCES CauHoi(MaCauHoi) ON DELETE CASCADE,
    NhanLuaChon CHAR(1) NOT NULL, -- 'A','B','C','D'
    NoiDung NVARCHAR(MAX) NOT NULL,
    LaDung BIT NOT NULL DEFAULT 0
);
GO

-- Liên kết Bài kiểm tra <-> Câu hỏi (có thể lộn xộn giữa Toán & TV)
CREATE TABLE BaiKiemTraCauHoi (
    MaBaiKiemTra INT NOT NULL REFERENCES BaiKiemTra(MaBaiKiemTra) ON DELETE CASCADE,
    MaCauHoi INT NOT NULL REFERENCES CauHoi(MaCauHoi),
    ThuTu TINYINT NOT NULL,
    CONSTRAINT PK_BaiKiemTraCauHoi PRIMARY KEY (MaBaiKiemTra, MaCauHoi)
);
GO

-- Tiến độ lý thuyết của người dùng / hoàn thành lý thuyết
CREATE TABLE TienDoLyThuyetNguoiDung (
    MaNguoiDung INT NOT NULL REFERENCES NguoiDung(MaNguoiDung),
    MaLyThuyet INT NOT NULL REFERENCES LyThuyet(MaLyThuyet),
    NgayHoanThanh DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME(),
    DiemThuong INT NOT NULL DEFAULT 0,
    CONSTRAINT PK_TienDoLyThuyetNguoiDung PRIMARY KEY (MaNguoiDung, MaLyThuyet)
);
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
GO

-- Đáp án từng câu cho mỗi lần thử
CREATE TABLE DapAnCauHoiNguoiDung (
    MaLanThu INT NOT NULL REFERENCES LanThuBaiKiemTraNguoiDung(MaLanThu) ON DELETE CASCADE,
    MaCauHoi INT NOT NULL REFERENCES CauHoi(MaCauHoi),
    MaLuaChonChon INT NULL REFERENCES LuaChon(MaLuaChon),
    LaDung BIT NOT NULL,
    CONSTRAINT PK_DapAnCauHoiNguoiDung PRIMARY KEY (MaLanThu, MaCauHoi)
);
GO

-- Trò chơi
CREATE TABLE TroChoi (
    MaTroChoi INT IDENTITY(1,1) PRIMARY KEY,
    TenTroChoi NVARCHAR(150) NOT NULL, -- 'liên hoàn tính toán' ...
    MoTa NVARCHAR(MAX) NULL,
    DiemThanhCong INT NOT NULL DEFAULT 80
);
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
GO

-- Thử thách
CREATE TABLE ThuThach (
    MaThuThach INT IDENTITY(1,1) PRIMARY KEY,
    TieuDe NVARCHAR(200) NOT NULL,
    MoTa NVARCHAR(MAX) NULL,
    Diem INT NOT NULL DEFAULT 80
);
GO

CREATE TABLE LanThuThuThach (
    MaLanThuThuThach INT IDENTITY(1,1) PRIMARY KEY,
    MaNguoiDung INT NOT NULL REFERENCES NguoiDung(MaNguoiDung),
    MaThuThach INT NOT NULL REFERENCES ThuThach(MaThuThach),
    Diem INT NOT NULL,
    NgayHoanThanh DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME()
);
GO

-- Thông báo (cài đặt thông báo & lưu thông báo)
CREATE TABLE ThongBao (
    MaThongBao INT IDENTITY(1,1) PRIMARY KEY,
    MaNguoiDung INT NULL REFERENCES NguoiDung(MaNguoiDung),
    NoiDung NVARCHAR(500) NOT NULL,
    DaDoc BIT DEFAULT 0,
    NgayTao DATETIME2 DEFAULT SYSUTCDATETIME()
);
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

-- Insert VaiTro
INSERT INTO VaiTro (MaVaiTro, TenVaiTro) VALUES (1,'Admin'),(2,'NguoiDung');
GO

-- Insert NguoiDung
INSERT INTO NguoiDung (TenDangNhap, Email, MatKhauMaHoa, MaVaiTro)
VALUES 
    (N'Thọ Khang', N'khangheheqt@gmail.com', N'123456', 2),
    (N'AdminTest', N'admin@example.com', N'admin123', 1),
    (N'UserOne', N'user1@example.com', N'password1', 2),
    (N'UserTwo', N'user2@example.com', N'password2', 2);
GO

-- Insert MonHoc
INSERT INTO MonHoc (MaMonHoc, TenMonHoc) VALUES (1,'Toán'),(2,'Tiếng Việt');
GO

-- Insert LyThuyet: 10 Toán, 10 Tiếng Việt
-- Toán
INSERT INTO LyThuyet (MaMonHoc, TieuDe, NoiDung) VALUES
(1, N'Cộng số tự nhiên 1', N'Cộng hai số a + b = c, ví dụ 2 + 3 = 5. Đây là phép tính cơ bản.'),
(1, N'Cộng số tự nhiên 2', N'Cộng với số lớn hơn, ví dụ 10 + 5 = 15. Học thuộc bảng cộng.'),
(1, N'Trừ số tự nhiên 1', N'Trừ a - b = c, ví dụ 5 - 2 = 3. a phải lớn hơn b.'),
(1, N'Trừ số tự nhiên 2', N'Trừ với số lớn, ví dụ 20 - 8 = 12. Sử dụng mượn.'),
(1, N'Nhân số tự nhiên 1', N'Nhân a * b = c, ví dụ 4 * 3 = 12.'),
(1, N'Nhân số tự nhiên 2', N'Bảng cửu chương 2-5 để nhân nhanh.'),
(1, N'Chia số tự nhiên 1', N'Chia a / b = c, ví dụ 10 / 2 = 5.'),
(1, N'Chia số tự nhiên 2', N'Chia có dư, ví dụ 13 / 3 = 4 dư 1.'),
(1, N'Hình học cơ bản 1', N'Nhận biết hình vuông, hình tròn, hình tam giác.'),
(1, N'Hình học cơ bản 2', N'Tính chu vi hình chữ nhật: 2*(dài + rộng).');
-- Tiếng Việt
INSERT INTO LyThuyet (MaMonHoc, TieuDe, NoiDung) VALUES
(2, N'Chữ cái alphabet 1', N'A B C D E F G. Học thuộc lòng chữ cái đầu.'),
(2, N'Chữ cái alphabet 2', N'H I J K L M N. Thực hành viết chữ cái.'),
(2, N'Từ ghép 1', N'Nhà cửa, cây cối. Từ ghép từ hai từ đơn.'),
(2, N'Từ ghép 2', N'Sách vở, bàn ghế. Ví dụ từ ghép trong đời sống.'),
(2, N'Câu đơn 1', N'Con mèo chạy. Câu có chủ ngữ và vị ngữ.'),
(2, N'Câu đơn 2', N'Con chó sủa. Thực hành tạo câu đơn.'),
(2, N'Từ đồng nghĩa 1', N'Vui vẻ - hạnh phúc. Từ có nghĩa giống nhau.'),
(2, N'Từ đồng nghĩa 2', N'Nhanh - chóng. Sử dụng từ đồng nghĩa trong câu.'),
(2, N'Từ trái nghĩa 1', N'Nóng - lạnh. Từ có nghĩa ngược nhau.'),
(2, N'Từ trái nghĩa 2', N'Cao - thấp. Ví dụ từ trái nghĩa.');
GO

-- Insert BaiKiemTra: 10 củng cố Toán, 10 TV, 6 ôn tập hỗn hợp, 1 thử thách (hỗn hợp MaMonHoc=NULL)
DECLARE @i INT = 1;
WHILE @i <= 10
BEGIN
    INSERT INTO BaiKiemTra (TieuDe, LoaiBaiKiemTra, MaMonHoc, CoXaoTron, TongSoCauHoi) 
    VALUES (N'Củng cố Toán ' + CAST(@i AS NVARCHAR(10)), 'CungCo', 1, 1, 10);
    SET @i = @i + 1;
END
GO
DECLARE @i INT = 1;
WHILE @i <= 10
BEGIN
    INSERT INTO BaiKiemTra (TieuDe, LoaiBaiKiemTra, MaMonHoc, CoXaoTron, TongSoCauHoi) 
    VALUES (N'Củng cố Tiếng Việt ' + CAST(@i AS NVARCHAR(10)), 'CungCo', 2, 1, 10);
    SET @i = @i + 1;
END
GO
INSERT INTO BaiKiemTra (TieuDe, LoaiBaiKiemTra, MaMonHoc, CoXaoTron, TongSoCauHoi) VALUES 
(N'Ôn tập hỗn hợp - cơ bản 1','OnTapCoBan',NULL,1,10),
(N'Ôn tập hỗn hợp - cơ bản 2','OnTapCoBan',NULL,1,10),
(N'Ôn tập hỗn hợp - trung bình 1','OnTapTrungBinh',NULL,1,10),
(N'Ôn tập hỗn hợp - trung bình 2','OnTapTrungBinh',NULL,1,10),
(N'Ôn tập hỗn hợp - nâng cao 1','OnTapNangCao',NULL,1,10),
(N'Ôn tập hỗn hợp - nâng cao 2','OnTapNangCao',NULL,1,10),
(N'Thử thách chọn đáp án A/B/C/D 1','ThuThach',NULL,1,10);
GO

-- Insert CauHoi: 50 Toán, 50 TV, 20 hỗn hợp
-- Toán: 50 câu (giả)
DECLARE @i INT = 1;
WHILE @i <= 50
BEGIN
    INSERT INTO CauHoi (MaMonHoc, NoiDung, GiaiThich) VALUES
    (1, N'Câu Toán ' + CAST(@i AS NVARCHAR(10)) + N': ' + CAST(@i AS NVARCHAR(10)) + N' + ' + CAST(@i+1 AS NVARCHAR(10)) + N' = ?', CAST(@i + @i + 1 AS NVARCHAR(10)));
    SET @i = @i + 1;
END
GO
-- Tiếng Việt: 50 câu (giả)
DECLARE @i INT = 1;
WHILE @i <= 50
BEGIN
    INSERT INTO CauHoi (MaMonHoc, NoiDung, GiaiThich) VALUES
    (2, N'Câu Tiếng Việt ' + CAST(@i AS NVARCHAR(10)) + N': Chữ cái thứ ' + CAST(@i AS NVARCHAR(10)) + N' là?', N'Chữ cái ' + CAST(@i AS NVARCHAR(10)));
    SET @i = @i + 1;
END
GO
-- Hỗn hợp: 20 câu
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

-- Insert LuaChon: 4 lựa chọn per câu hỏi, random LaDung
DECLARE @total_cau INT = (SELECT COUNT(*) FROM CauHoi);
DECLARE @cau_id INT = 1;
WHILE @cau_id <= @total_cau
BEGIN
    DECLARE @vi_tri_dung INT = ABS(CHECKSUM(NEWID()) % 4) + 1;
    DECLARE @nhan CHAR(1), @la_dung BIT, @j INT = 1;
    WHILE @j <= 4
    BEGIN
        SET @nhan = CASE @j WHEN 1 THEN 'A' WHEN 2 THEN 'B' WHEN 3 THEN 'C' ELSE 'D' END;
        SET @la_dung = CASE WHEN @j = @vi_tri_dung THEN 1 ELSE 0 END;
        INSERT INTO LuaChon (MaCauHoi, NhanLuaChon, NoiDung, LaDung) VALUES
        (@cau_id, @nhan, N'Đáp án ' + @nhan + N' cho câu ' + CAST(@cau_id AS NVARCHAR(10)), @la_dung);
        SET @j = @j + 1;
    END
    SET @cau_id = @cau_id + 1;
END
GO

-- Insert BaiKiemTraCauHoi: Mỗi bài 10 câu, tránh duplicate bằng cách dùng temp table check tồn tại
-- Củng cố Toán (1-10): Random từ 1-50 (Toán)
DECLARE @bai_id INT = 1;
WHILE @bai_id <= 10
BEGIN
    DECLARE @thu_tu TINYINT = 1;
    DECLARE @selected_cau TABLE (MaCauHoi INT);
    WHILE @thu_tu <= 10
    BEGIN
        DECLARE @random_cau_toan INT = ABS(CHECKSUM(NEWID()) % 50) + 1;
        WHILE EXISTS (SELECT 1 FROM @selected_cau WHERE MaCauHoi = @random_cau_toan)
        BEGIN
            SET @random_cau_toan = ABS(CHECKSUM(NEWID()) % 50) + 1;
        END
        INSERT INTO @selected_cau (MaCauHoi) VALUES (@random_cau_toan);
        INSERT INTO BaiKiemTraCauHoi (MaBaiKiemTra, MaCauHoi, ThuTu) VALUES (@bai_id, @random_cau_toan, @thu_tu);
        SET @thu_tu = @thu_tu + 1;
    END
    SET @bai_id = @bai_id + 1;
END
GO
-- Củng cố TV (11-20): Random từ 51-100 (TV)
SET @bai_id = 11;
WHILE @bai_id <= 20
BEGIN
    SET @thu_tu = 1;
    DELETE FROM @selected_cau;  -- Clear temp
    WHILE @thu_tu <= 10
    BEGIN
        DECLARE @random_cau_tv INT = ABS(CHECKSUM(NEWID()) % 50) + 51;
        WHILE EXISTS (SELECT 1 FROM @selected_cau WHERE MaCauHoi = @random_cau_tv)
        BEGIN
            SET @random_cau_tv = ABS(CHECKSUM(NEWID()) % 50) + 51;
        END
        INSERT INTO @selected_cau (MaCauHoi) VALUES (@random_cau_tv);
        INSERT INTO BaiKiemTraCauHoi (MaBaiKiemTra, MaCauHoi, ThuTu) VALUES (@bai_id, @random_cau_tv, @thu_tu);
        SET @thu_tu = @thu_tu + 1;
    END
    SET @bai_id = @bai_id + 1;
END
GO
-- Ôn tập hỗn hợp/thử thách (21-27): Random từ 1-120, tránh duplicate
SET @bai_id = 21;
WHILE @bai_id <= 27
BEGIN
    SET @thu_tu = 1;
    DELETE FROM @selected_cau;  -- Clear temp
    WHILE @thu_tu <= 10
    BEGIN
        DECLARE @random_cau_honhop INT = ABS(CHECKSUM(NEWID()) % 120) + 1;
        WHILE EXISTS (SELECT 1 FROM @selected_cau WHERE MaCauHoi = @random_cau_honhop)
        BEGIN
            SET @random_cau_honhop = ABS(CHECKSUM(NEWID()) % 120) + 1;
        END
        INSERT INTO @selected_cau (MaCauHoi) VALUES (@random_cau_honhop);
        INSERT INTO BaiKiemTraCauHoi (MaBaiKiemTra, MaCauHoi, ThuTu) VALUES (@bai_id, @random_cau_honhop, @thu_tu);
        SET @thu_tu = @thu_tu + 1;
    END
    SET @bai_id = @bai_id + 1;
END
GO

-- Insert TienDoLyThuyetNguoiDung
INSERT INTO TienDoLyThuyetNguoiDung (MaNguoiDung, MaLyThuyet, DiemThuong) VALUES (1, 1, 10), (1, 2, 10);
GO

-- Insert LanThuBaiKiemTraNguoiDung
INSERT INTO LanThuBaiKiemTraNguoiDung (MaNguoiDung, MaBaiKiemTra, Diem, DiemToiDa) VALUES (1, 1, 40, 50);
GO

-- Insert DapAnCauHoiNguoiDung: Sử dụng random MaLuaChon thực từ LuaChon của câu hỏi
DECLARE @lan_thu INT = 1, @cau_id INT = 1;
WHILE @cau_id <= 10
BEGIN
    DECLARE @la_dung BIT = CASE WHEN @cau_id % 2 = 1 THEN 1 ELSE 0 END;
    DECLARE @ma_lua INT = (SELECT TOP 1 MaLuaChon FROM LuaChon WHERE MaCauHoi = @cau_id AND LaDung = @la_dung ORDER BY NEWID());
    INSERT INTO DapAnCauHoiNguoiDung (MaLanThu, MaCauHoi, MaLuaChonChon, LaDung) VALUES (@lan_thu, @cau_id, @ma_lua, @la_dung);
    SET @cau_id = @cau_id + 1;
END
GO

-- Insert TroChoi
INSERT INTO TroChoi (TenTroChoi, MoTa, DiemThanhCong)
VALUES
(N'Liên hoàn tính toán',N'Chuỗi phép tính nhỏ, đúng hết được điểm',80),
(N'Hoàn thiện câu từ',N'Điền vào chỗ trống',80),
(N'Trùm tính toán',N'Chuỗi bài nâng cao',80);
GO

-- Insert LanThuTroChoi
INSERT INTO LanThuTroChoi (MaNguoiDung, MaTroChoi, Diem, ThanhCong) VALUES (1, 1, 80, 1);
GO

-- Insert ThuThach
INSERT INTO ThuThach (TieuDe, MoTa, Diem)
VALUES
('Thử thách 1','Đề chọn đáp án A/B/C/D',80),
('Thử thách 2','Đề chọn đáp án A/B/C/D',80);
GO

-- Insert LanThuThuThach
INSERT INTO LanThuThuThach (MaNguoiDung, MaThuThach, Diem) VALUES (1, 1, 80);
GO

-- Insert ThongBao
INSERT INTO ThongBao (MaNguoiDung, NoiDung) VALUES (1, N'Chào mừng bạn đến với ứng dụng!'), (2, N'Thông báo cho admin');
GO

-- Insert LichHenGio
INSERT INTO LichHenGio (MaNguoiDung, ThoiGianHen, LapLai) VALUES (1, '08:00:00', N'HangNgay');
GO

-- Insert HoatDongHangNgay
INSERT INTO HoatDongHangNgay (MaNguoiDung, NgayHoatDong, DaDangNhap, DiemKiemDuoc) VALUES (1, CAST(SYSDATETIME() AS DATE), 1, 20);
GO

-- Kiểm tra count
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