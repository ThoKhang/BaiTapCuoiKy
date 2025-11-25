-- DROP + CREATE DB sạch
USE master;
IF EXISTS (SELECT name FROM sys.databases WHERE name = 'UngDungHocTapChoTre')
BEGIN
    ALTER DATABASE UngDungHocTapChoTre SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE UngDungHocTapChoTre;
END;
GO
CREATE DATABASE UngDungHocTapChoTre;
GO
USE UngDungHocTapChoTre;
GO

---------------------------------------------------------------------
-- DDL: TẠO BẢNG
---------------------------------------------------------------------

-- 1. Bảng NguoiDung
CREATE TABLE NguoiDung (
    MaNguoiDung char(5) PRIMARY KEY,
    TenDangNhap NVARCHAR(100) NOT NULL UNIQUE,
    Email NVARCHAR(255) UNIQUE,
    MatKhauMaHoa NVARCHAR(512) NOT NULL,
    NgayTao DATETIME2 DEFAULT SYSUTCDATETIME(),
    LanDangNhapCuoi DATETIME2 NULL,
    SoLanTrucTuyen INT DEFAULT 0,
    TongDiem INT DEFAULT 0
);
GO

-- 2. Bảng MonHoc (Subject)
CREATE TABLE MonHoc (
    MaMonHoc char(5) PRIMARY KEY,
    TenMonHoc NVARCHAR(50) NOT NULL UNIQUE
);
GO

-- 3. Bảng LoaiHoatDong (Activity Type: LyThuyet, OnLuyen, CungCo, ThuThach, TroChoi)
CREATE TABLE LoaiHoatDong (
    MaLoai char(5) PRIMARY KEY,
    TenLoai NVARCHAR(50) NOT NULL UNIQUE,
    MoTaLoai NVARCHAR(100) NULL
);
GO

-- 4. Bảng HoatDongHocTap (Learning Activity - Supertype)
CREATE TABLE HoatDongHocTap (
    MaHoatDong char(5) PRIMARY KEY,
    MaMonHoc char(5) FOREIGN KEY REFERENCES MonHoc(MaMonHoc),
    MaLoai char(5) FOREIGN KEY REFERENCES LoaiHoatDong(MaLoai),
    TieuDe NVARCHAR(100) NOT NULL,
    MoTa NVARCHAR(300) NULL,
    TongDiemToiDa INT NOT NULL
);
GO

-- 5. Bảng CauHoi (Question)
CREATE TABLE CauHoi (
    MaCauHoi char(5) PRIMARY KEY,
    NoiDungCauHoi NVARCHAR(300) NOT NULL,
    GiaiThich NVARCHAR(300) NULL,
    DiemToiDa INT NOT NULL -- Điểm tối đa cho 1 câu hỏi
);
GO

-- 6. Bảng DapAn (Answer)
CREATE TABLE DapAn (
    MaDapAn char(6) PRIMARY KEY,
    MaCauHoi char(5) FOREIGN KEY REFERENCES CauHoi(MaCauHoi),
    NoiDungDapAn NVARCHAR(100) NOT NULL,
    LaDapAnDung BIT NOT NULL -- Sử dụng BIT (1=Đúng, 0=Sai)
);
GO

-- 7. Bảng HoatDong_CauHoi (M:N link giữa Hoạt động và Câu hỏi)
CREATE TABLE HoatDong_CauHoi (
    MaHoatDong char(5) FOREIGN KEY REFERENCES HoatDongHocTap(MaHoatDong),
    MaCauHoi char(5) FOREIGN KEY REFERENCES CauHoi(MaCauHoi),
    ThuTu INT NOT NULL,
    PRIMARY KEY (MaHoatDong, MaCauHoi)
);
GO

-- 8. Bảng TienTrinhHocTap (Learning Progress - Thay thế BaiHoc)
CREATE TABLE TienTrinhHocTap (
    MaTienTrinh char(5) PRIMARY KEY,
    MaNguoiDung char(5) FOREIGN KEY REFERENCES NguoiDung(MaNguoiDung),
    MaHoatDong char(5) FOREIGN KEY REFERENCES HoatDongHocTap(MaHoatDong),
    NgayBatDau DATETIME2 DEFAULT SYSUTCDATETIME(),
    NgayHoanThanh DATETIME2 NULL,
    SoCauDung INT DEFAULT 0,
    SoCauDaLam INT DEFAULT 0,
    DiemDatDuoc INT DEFAULT 0,
    DaHoanThanh BIT NOT NULL DEFAULT 0
);
GO
-- 9. bảng LichSuHoatDong
CREATE TABLE LichSuHoatDong (
    MaLichSu char(8) PRIMARY KEY,
    MaNguoiDung char(5) FOREIGN KEY REFERENCES NguoiDung(MaNguoiDung),
    ThoiGian DATETIME2 DEFAULT SYSUTCDATETIME(),
    LoaiDiem NVARCHAR(50) NOT NULL, -- Ví dụ: 'Kiểm tra', 'Hàng ngày', 'Thưởng'
    SoDiem INT NOT NULL,
    ChiTiet NVARCHAR(200) NULL
);
GO

---------------------------------------------------------------------
-- DML: INSERT DỮ LIỆU
---------------------------------------------------------------------

-- 1) NguoiDung
INSERT INTO NguoiDung (MaNguoiDung, TenDangNhap, Email, MatKhauMaHoa, TongDiem) VALUES
('ND001', N'huy', N'huy@gmail.com', N'123', 0),
('ND002', N'minhanh', N'minhanh@gmail.com', N'hash', 55),
('ND003', N'tuan', N'tuan@gmail.com', N'hash', 50),
('ND004', N'lananh', N'lananh@gmail.com', N'hash', 140),
('ND005', N'khang', N'khangheheqt@gmail.com', N'123', 35);
GO

-- 2) MonHoc
INSERT INTO MonHoc (MaMonHoc, TenMonHoc) VALUES
('MH001', N'Toán'),
('MH002', N'Tiếng Việt');
GO

-- 3) LoaiHoatDong
INSERT INTO LoaiHoatDong (MaLoai, TenLoai, MoTaLoai) VALUES
('LHD01', N'Lý Thuyết', N'Bài học lý thuyết, kiến thức nền tảng.'),
('LHD02', N'Củng Cố', N'Bài tập củng cố kiến thức.'),
('LHD03', N'Ôn Luyện', N'Bài tập tổng hợp để ôn luyện.'),
('LHD04', N'Thử Thách', N'Bài tập nâng cao, kiểm tra.'),
('LHD05', N'Trò Chơi', N'Hoạt động học tập dưới dạng trò chơi.');
GO

-- Lấy IDs cho mapping
DECLARE @MaMH_Toan char(5) = (SELECT MaMonHoc FROM MonHoc WHERE TenMonHoc = N'Toán');
DECLARE @MaMH_TV char(5) = (SELECT MaMonHoc FROM MonHoc WHERE TenMonHoc = N'Tiếng Việt');
DECLARE @MaLoai_LT char(5) = (SELECT MaLoai FROM LoaiHoatDong WHERE TenLoai = N'Lý Thuyết');
DECLARE @MaLoai_CC char(5) = (SELECT MaLoai FROM LoaiHoatDong WHERE TenLoai = N'Củng Cố');
DECLARE @MaLoai_OL char(5) = (SELECT MaLoai FROM LoaiHoatDong WHERE TenLoai = N'Ôn Luyện');
DECLARE @MaLoai_TT char(5) = (SELECT MaLoai FROM LoaiHoatDong WHERE TenLoai = N'Thử Thách');

-- 4) HoatDongHocTap (Hợp nhất dữ liệu cũ)
-- a. LyThuyet (LT001-LT020)
INSERT INTO HoatDongHocTap (MaHoatDong, MaMonHoc, MaLoai, TieuDe, MoTa, TongDiemToiDa) VALUES
('LT001', @MaMH_Toan, @MaLoai_LT, N'Phép cộng', N'Cộng hai số', 50),
('LT002', @MaMH_Toan, @MaLoai_LT, N'Phép trừ', N'Trừ hai số', 50),
('LT003', @MaMH_Toan, @MaLoai_LT, N'Phép nhân', N'Bảng nhân', 50),
('LT004', @MaMH_Toan, @MaLoai_LT, N'Phép chia', N'Chia có dư', 50),
('LT005', @MaMH_Toan, @MaLoai_LT, N'Hình học', N'Hình vuông', 50),
('LT006', @MaMH_Toan, @MaLoai_LT, N'Đơn vị', N'Mét, kg', 50),
('LT007', @MaMH_Toan, @MaLoai_LT, N'Phân số', N'1/2 + 1/2', 50),
('LT008', @MaMH_Toan, @MaLoai_LT, N'Thập phân', N'0.1 + 0.2', 50),
('LT009', @MaMH_Toan, @MaLoai_LT, N'Thời gian', N'Đọc đồng hồ', 50),
('LT010', @MaMH_Toan, @MaLoai_LT, N'Tiền tệ', N'Cộng tiền', 50),
('LT011', @MaMH_TV, @MaLoai_LT, N'Bảng chữ cái', N'A-Z', 50),
('LT012', @MaMH_TV, @MaLoai_LT, N'Vần âm', N'a, ă, â', 50),
('LT013', @MaMH_TV, @MaLoai_LT, N'Từ đơn', N'Mẹ, bố', 50),
('LT014', @MaMH_TV, @MaLoai_LT, N'Từ ghép', N'Nhà cửa', 50),
('LT015', @MaMH_TV, @MaLoai_LT, N'Câu đơn', N'Câu kể', 50),
('LT016', @MaMH_TV, @MaLoai_LT, N'Dấu câu', N'Chấm, phẩy', 50),
('LT017', @MaMH_TV, @MaLoai_LT, N'Đồng âm', N'Trời', 50),
('LT018', @MaMH_TV, @MaLoai_LT, N'Trái nghĩa', N'Lớn - nhỏ', 50),
('LT019', @MaMH_TV, @MaLoai_LT, N'Thành ngữ', N'Công cha', 50),
('LT020', @MaMH_TV, @MaLoai_LT, N'Thơ', N'Con cò', 50);

-- b. CungCo (CC001-CC020)
INSERT INTO HoatDongHocTap (MaHoatDong, MaMonHoc, MaLoai, TieuDe, MoTa, TongDiemToiDa) VALUES
('CC001', @MaMH_Toan, @MaLoai_CC, N'Củng cố Toán 1', N'Củng cố kiến thức Toán cơ bản', 50),
('CC002', @MaMH_Toan, @MaLoai_CC, N'Củng cố Toán 2', N'Củng cố kiến thức Toán cơ bản', 50),
('CC003', @MaMH_Toan, @MaLoai_CC, N'Củng cố Toán 3', N'Củng cố kiến thức Toán cơ bản', 50),
('CC004', @MaMH_Toan, @MaLoai_CC, N'Củng cố Toán 4', N'Củng cố kiến thức Toán cơ bản', 50),
('CC005', @MaMH_Toan, @MaLoai_CC, N'Củng cố Toán 5', N'Củng cố kiến thức Toán cơ bản', 50),
('CC006', @MaMH_Toan, @MaLoai_CC, N'Củng cố Toán 6', N'Củng cố kiến thức Toán cơ bản', 50),
('CC007', @MaMH_Toan, @MaLoai_CC, N'Củng cố Toán 7', N'Củng cố kiến thức Toán cơ bản', 50),
('CC008', @MaMH_Toan, @MaLoai_CC, N'Củng cố Toán 8', N'Củng cố kiến thức Toán cơ bản', 50),
('CC009', @MaMH_Toan, @MaLoai_CC, N'Củng cố Toán 9', N'Củng cố kiến thức Toán cơ bản', 50),
('CC010', @MaMH_Toan, @MaLoai_CC, N'Củng cố Toán 10', N'Củng cố kiến thức Toán cơ bản', 50),
('CC011', @MaMH_TV, @MaLoai_CC, N'Củng cố TV 1', N'Củng cố kiến thức Tiếng Việt cơ bản', 50),
('CC012', @MaMH_TV, @MaLoai_CC, N'Củng cố TV 2', N'Củng cố kiến thức Tiếng Việt cơ bản', 50),
('CC013', @MaMH_TV, @MaLoai_CC, N'Củng cố TV 3', N'Củng cố kiến thức Tiếng Việt cơ bản', 50),
('CC014', @MaMH_TV, @MaLoai_CC, N'Củng cố TV 4', N'Củng cố kiến thức Tiếng Việt cơ bản', 50),
('CC015', @MaMH_TV, @MaLoai_CC, N'Củng cố TV 5', N'Củng cố kiến thức Tiếng Việt cơ bản', 50),
('CC016', @MaMH_TV, @MaLoai_CC, N'Củng cố TV 6', N'Củng cố kiến thức Tiếng Việt cơ bản', 50),
('CC017', @MaMH_TV, @MaLoai_CC, N'Củng cố TV 7', N'Củng cố kiến thức Tiếng Việt cơ bản', 50),
('CC018', @MaMH_TV, @MaLoai_CC, N'Củng cố TV 8', N'Củng cố kiến thức Tiếng Việt cơ bản', 50),
('CC019', @MaMH_TV, @MaLoai_CC, N'Củng cố TV 9', N'Củng cố kiến thức Tiếng Việt cơ bản', 50),
('CC020', @MaMH_TV, @MaLoai_CC, N'Củng cố TV 10', N'Củng cố kiến thức Tiếng Việt cơ bản', 50);

-- c. OnLuyen (OL001-OL009) - Gán tạm cho Toán
INSERT INTO HoatDongHocTap (MaHoatDong, MaMonHoc, MaLoai, TieuDe, MoTa, TongDiemToiDa) VALUES
('OL001', @MaMH_Toan, @MaLoai_OL, N'Ôn Cơ bản 1', N'Bài tập ôn luyện cơ bản', 50),
('OL002', @MaMH_Toan, @MaLoai_OL, N'Ôn Cơ bản 2', N'Bài tập ôn luyện cơ bản', 50),
('OL003', @MaMH_Toan, @MaLoai_OL, N'Ôn Cơ bản 3', N'Bài tập ôn luyện cơ bản', 50),
('OL004', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 1', N'Bài tập ôn luyện trung bình', 80),
('OL005', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 2', N'Bài tập ôn luyện trung bình', 80),
('OL006', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 3', N'Bài tập ôn luyện trung bình', 80),
('OL007', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 1', N'Bài tập ôn luyện nâng cao', 100),
('OL008', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 2', N'Bài tập ôn luyện nâng cao', 100),
('OL009', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 3', N'Bài tập ôn luyện nâng cao', 100);

-- d. ThuThach (TT001) - Gán tạm cho Toán
INSERT INTO HoatDongHocTap (MaHoatDong, MaMonHoc, MaLoai, TieuDe, MoTa, TongDiemToiDa) VALUES
('TT001', @MaMH_Toan, @MaLoai_TT, N'Siêu Thử Thách', N'20 câu khó nhất!', 100);
GO

-- 5) CauHoi (310 câu)
;WITH nums AS (
    SELECT TOP (310) ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS n
    FROM sys.all_objects a CROSS JOIN sys.all_objects b
)
INSERT INTO CauHoi (maCauHoi, NoiDungCauHoi, GiaiThich, DiemToiDa)
SELECT
    'CH' + RIGHT('000' + CAST(n AS varchar(10)),3),
    CASE WHEN n <= 155 THEN N'Toán: Câu ' + CAST(n AS nvarchar(10)) ELSE N'TV: Câu ' + CAST(n AS nvarchar(10)) END,
    N'Giải thích ' + CAST(n AS nvarchar(10)),
    5
FROM nums;
GO

-- 6) DapAn (4 đáp án cho mỗi câu)
;WITH nums AS (
    SELECT TOP (310) ROW_NUMBER() OVER (ORDER BY (SELECT NULL)) AS qnum
    FROM sys.all_objects a CROSS JOIN sys.all_objects b
),
ans AS (
    SELECT
        qnum,
        v.v AS ansIndex,
        ((qnum-1)*4 + v.v) AS dapSo
    FROM nums
    CROSS APPLY (VALUES (1),(2),(3),(4)) v(v)
)
INSERT INTO DapAn (maDapAn, maCauHoi, NoiDungDapAn, LaDapAnDung)
SELECT
    'DA' + RIGHT('0000' + CAST(dapSo AS varchar(10)),4),
    'CH' + RIGHT('000' + CAST(qnum AS varchar(10)),3),
    CASE ansIndex WHEN 1 THEN N'A' WHEN 2 THEN N'B' WHEN 3 THEN N'C' ELSE N'D' END,
    CASE WHEN ((qnum % 4) + 1) = ansIndex THEN 1 ELSE 0 END
FROM ans;
GO

-- 7) HoatDong_CauHoi (Sử dụng ID câu hỏi duy nhất để đảm bảo tính toàn vẹn)
-- a. CungCo (CC001 - CC020) -> Sử dụng CH001 - CH200
DECLARE @cc_fix INT = 1, @ch_fix INT = 1;
WHILE @cc_fix <= 20
BEGIN
    DECLARE @thuTu_cc_fix INT = 1;
    WHILE @thuTu_cc_fix <= 10
    BEGIN
        INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
        VALUES ('CC' + RIGHT('000' + CAST(@cc_fix AS VARCHAR), 3), 'CH' + RIGHT('000' + CAST(@ch_fix AS VARCHAR), 3), @thuTu_cc_fix);
        SET @ch_fix = @ch_fix + 1;
        SET @thuTu_cc_fix = @thuTu_cc_fix + 1;
    END
    SET @cc_fix = @cc_fix + 1;
END
GO

-- b. OnLuyen (OL001 - OL009) -> Sử dụng CH201 - CH290
DECLARE @ol_fix INT = 1, @ch_ol_fix INT = 201;
WHILE @ol_fix <= 9
BEGIN
    DECLARE @thuTu_ol_fix INT = 1;
    WHILE @thuTu_ol_fix <= 10
    BEGIN
        INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
        VALUES ('OL' + RIGHT('000' + CAST(@ol_fix AS VARCHAR), 3), 'CH' + RIGHT('000' + CAST(@ch_ol_fix AS VARCHAR), 3), @thuTu_ol_fix);
        SET @ch_ol_fix = @ch_ol_fix + 1;
        SET @thuTu_ol_fix = @thuTu_ol_fix + 1;
    END
    SET @ol_fix = @ol_fix + 1;
END
GO

-- c. ThuThach TT001 -> Sử dụng CH291 - CH310
DECLARE @ch_tt_fix INT = 291;
DECLARE @thuTu_tt_fix INT = 1;
WHILE @thuTu_tt_fix <= 20
BEGIN
    INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
    VALUES ('TT001', 'CH' + RIGHT('000' + CAST(@ch_tt_fix AS VARCHAR), 3), @thuTu_tt_fix);
    SET @ch_tt_fix = @ch_tt_fix + 1;
    SET @thuTu_tt_fix = @thuTu_tt_fix + 1;
END
GO

-- 8) TienTrinhHocTap (Mapping dữ liệu BaiHoc cũ)
INSERT INTO TienTrinhHocTap (MaTienTrinh, MaNguoiDung, MaHoatDong, SoCauDung, SoCauDaLam, DaHoanThanh, DiemDatDuoc) VALUES
('TT001', 'ND002', 'CC001', 8, 10, 1, 40), -- MaHoatDong = maCungCo
('TT002', 'ND002', 'CC002', 3, 5, 0, 15),
('TT003', 'ND003', 'CC001', 10, 10, 1, 50),
('TT004', 'ND004', 'CC001', 10, 10, 1, 50),
('TT005', 'ND004', 'TT001', 18, 20, 1, 90), -- MaHoatDong = maThuThach
('TT006', 'ND005', 'CC001', 7, 10, 1, 35);
GO

PRINT 'HOÀN TẤT! Database đã được thiết kế lại và làm sạch thành công. Vui lòng kiểm tra các bảng sau.'
GO

-- Kiểm tra dữ liệu
SELECT * FROM NguoiDung;
SELECT * FROM MonHoc;
SELECT * FROM LoaiHoatDong;
SELECT * FROM HoatDongHocTap WHERE MaHoatDong IN ('LT001', 'CC001', 'OL001', 'TT001');
SELECT * FROM TienTrinhHocTap;
-- Kiểm tra liên kết Hoạt động và Câu hỏi
SELECT * FROM HoatDong_CauHoi WHERE MaHoatDong IN ('CC001', 'OL001', 'TT001');
GO

--==========================================================TẤT CẢ TRUY VẤN NẰM Ở ĐÂY=======================================================================
-- 1. Lấy ra tất cả các bài học của Củng Cố
SELECT 
    HD.MaHoatDong, 
    HD.TieuDe, 
    MH.TenMonHoc
FROM 
    HoatDongHocTap HD
JOIN 
    LoaiHoatDong LHD ON HD.MaLoai = LHD.MaLoai
JOIN
    MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
WHERE 
    LHD.TenLoai = N'Củng Cố'; -- Hoặc WHERE HD.MaLoai = 'LHD02'

-- 2. Lấy ra tất cả các bài học của Ôn Luyện (Luyện tập)
SELECT 
    HD.MaHoatDong, 
    HD.TieuDe, 
    MH.TenMonHoc
FROM 
    HoatDongHocTap HD
JOIN 
    LoaiHoatDong LHD ON HD.MaLoai = LHD.MaLoai
JOIN
    MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
WHERE 
    LHD.TenLoai = N'Ôn Luyện'; -- Hoặc WHERE HD.MaLoai = 'LHD03'

-- 3. Lấy ra tất cả các bài học của Thử Thách
SELECT 
    HD.MaHoatDong, 
    HD.TieuDe, 
    MH.TenMonHoc
FROM 
    HoatDongHocTap HD
JOIN 
    LoaiHoatDong LHD ON HD.MaLoai = LHD.MaLoai
JOIN
    MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
WHERE 
    LHD.TenLoai = N'Thử Thách'; -- Hoặc WHERE HD.MaLoai = 'LHD04'

-- 4. Lấy ra tất cả các bài học của Trò Chơi
SELECT 
    HD.MaHoatDong, 
    HD.TieuDe, 
    MH.TenMonHoc
FROM 
    HoatDongHocTap HD
JOIN 
    LoaiHoatDong LHD ON HD.MaLoai = LHD.MaLoai
JOIN
    MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
WHERE 
    LHD.TenLoai = N'Trò Chơi'; -- Hoặc WHERE HD.MaLoai = 'LHD05'

-- 5. Lấy ra tất cả các bài học Củng Cố MÀ NGƯỜI DÙNG ĐÃ LÀM (cho người dùng ND001)
SELECT
    ND.TenDangNhap,
    HD.TieuDe AS TenBaiHoc,
    MH.TenMonHoc,
    TT.DaHoanThanh
FROM
    TienTrinhHocTap TT
JOIN
    NguoiDung ND ON TT.MaNguoiDung = ND.MaNguoiDung
JOIN
    HoatDongHocTap HD ON TT.MaHoatDong = HD.MaHoatDong
JOIN
    MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
WHERE
    ND.MaNguoiDung = 'ND001' AND HD.MaLoai = 'LHD02' -- LHD02: Củng Cố
ORDER BY
    TT.NgayBatDau DESC;

-- 6a. Lấy ra tất cả các bài Ôn Luyện (Luyện tập) mà người dùng đã làm (cho người dùng ND004)
SELECT
    HD.TieuDe,
    TT.SoCauDung,
    TT.DiemDatDuoc,
    TT.DaHoanThanh
FROM
    TienTrinhHocTap TT
JOIN
    HoatDongHocTap HD ON TT.MaHoatDong = HD.MaHoatDong
WHERE
    TT.MaNguoiDung = 'ND004' AND HD.MaLoai = 'LHD03'; -- LHD03: Ôn Luyện

-- 6b. Lấy ra tất cả các bài Thử Thách mà người dùng đã làm (cho người dùng ND004)
SELECT
    HD.TieuDe,
    TT.SoCauDung,
    TT.DiemDatDuoc,
    TT.DaHoanThanh
FROM
    TienTrinhHocTap TT
JOIN
    HoatDongHocTap HD ON TT.MaHoatDong = HD.MaHoatDong
WHERE
    TT.MaNguoiDung = 'ND004' AND HD.MaLoai = 'LHD04'; -- LHD04: Thử Thách

-- 6c. Lấy ra tất cả các bài Trò Chơi mà người dùng đã làm (LƯU Ý: Không có dữ liệu mẫu cho LHD05)
SELECT
    HD.TieuDe,
    TT.SoCauDung,
    TT.DiemDatDuoc,
    TT.DaHoanThanh
FROM
    TienTrinhHocTap TT
JOIN
    HoatDongHocTap HD ON TT.MaHoatDong = HD.MaHoatDong
WHERE
    TT.MaNguoiDung = 'ND004' AND HD.MaLoai = 'LHD05'; -- LHD05: Trò Chơi


-- 7. Lấy ra được TỔNG SỐ CÂU HỎI mà người dùng đã làm ở Củng cố Toán 1 (CC001)
SELECT
    TT.MaNguoiDung,
    ND.TenDangNhap,
    HD.TieuDe AS TenHoatDong,
    TT.SoCauDaLam AS TongSoCauDaLam
FROM
    TienTrinhHocTap TT
JOIN
    NguoiDung ND ON TT.MaNguoiDung = ND.MaNguoiDung
JOIN
    HoatDongHocTap HD ON TT.MaHoatDong = HD.MaHoatDong
WHERE
    HD.TieuDe = N'Củng cố Toán 1' AND HD.MaLoai = 'LHD02';

-- Lấy 10 người đứng đầu
SELECT
    TOP 10 
    ROW_NUMBER() OVER (ORDER BY TongDiem DESC) AS Hang,
    TenDangNhap,
    TongDiem
FROM
    NguoiDung
ORDER BY
    TongDiem DESC;

--Truy vấn này lấy ra tất cả các hoạt động (Lý Thuyết, Củng Cố, Ôn Luyện) của một môn học cụ thể
SELECT
    HD.MaHoatDong,
    HD.TieuDe,
    LHD.TenLoai,
    HD.TongDiemToiDa,
    CASE
        WHEN TT.MaNguoiDung IS NOT NULL THEN N'Đã làm (' + CAST(TT.DiemDatDuoc AS NVARCHAR) + N' điểm)'
        ELSE N'Chưa làm'
    END AS TrangThai,
    TT.DaHoanThanh
FROM
    HoatDongHocTap HD
JOIN
    MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
JOIN
    LoaiHoatDong LHD ON HD.MaLoai = LHD.MaLoai
LEFT JOIN -- Dùng LEFT JOIN để lấy cả những bài chưa làm
    TienTrinhHocTap TT ON HD.MaHoatDong = TT.MaHoatDong AND TT.MaNguoiDung = 'ND002' -- Thay 'ND002' bằng người dùng hiện tại
WHERE
    MH.TenMonHoc = N'Toán' -- Hoặc MH.MaMonHoc = 'MH001'
ORDER BY
    LHD.MaLoai, HD.MaHoatDong;

--10. Lấy Chi Tiết Nội Dung Một Bài Học/Hoạt Động (Ví dụ: Củng cố Toán 1 - CC001)
SELECT
    HD.TieuDe AS TenHoatDong,
    CH.MaCauHoi,
    CH.NoiDungCauHoi,
    DAP.MaDapAn,
    DAP.NoiDungDapAn,
    DAP.LaDapAnDung, -- CHỈ HIỂN THỊ TRƯỜNG NÀY Ở MÀN HÌNH ADMIN/KHI GIẢI THÍCH
    CHQH.ThuTu
FROM
    HoatDongHocTap HD
JOIN
    HoatDong_CauHoi CHQH ON HD.MaHoatDong = CHQH.MaHoatDong
JOIN
    CauHoi CH ON CHQH.MaCauHoi = CH.MaCauHoi
JOIN
    DapAn DAP ON CH.MaCauHoi = DAP.MaCauHoi
WHERE
    HD.MaHoatDong = 'CC001' -- Thay bằng MaHoatDong người dùng chọn
ORDER BY
    CHQH.ThuTu, DAP.MaDapAn;

-- 11. Tổng Số Câu Hỏi Theo Loại Hoạt Động 
SELECT
    LHD.TenLoai,
    HD.MaHoatDong,
    HD.TieuDe AS TenHoatDong,
    COUNT(HQC.MaCauHoi) AS TongSoCauHoi
FROM
    LoaiHoatDong LHD
JOIN
    HoatDongHocTap HD ON LHD.MaLoai = HD.MaLoai
LEFT JOIN
    HoatDong_CauHoi HQC ON HD.MaHoatDong = HQC.MaHoatDong
WHERE
    LHD.MaLoai IN ('LHD01', 'LHD02', 'LHD03', 'LHD04', 'LHD05')
GROUP BY
    LHD.TenLoai, 
    LHD.MaLoai, -- ĐÃ THÊM VÀO GROUP BY
    HD.MaHoatDong, 
    HD.TieuDe
ORDER BY
    LHD.MaLoai, 
    TongSoCauHoi DESC, 
    HD.TieuDe;

--Cau hoi của củng cố
SELECT
    COUNT(HQC.MaCauHoi) AS TongSoCauHoiCungCo
FROM
    HoatDongHocTap HD
LEFT JOIN 
    HoatDong_CauHoi HQC ON HD.MaHoatDong = HQC.MaHoatDong
WHERE
    HD.MaLoai = 'LHD02'; -- LHD02: Củng Cố

--Tổng số câu hỏi của Ôn Luyện
SELECT
    COUNT(HQC.MaCauHoi) AS TongSoCauHoiOnLuyen
FROM
    HoatDongHocTap HD
LEFT JOIN 
    HoatDong_CauHoi HQC ON HD.MaHoatDong = HQC.MaHoatDong
WHERE
    HD.MaLoai = 'LHD03'; -- LHD03: Ôn Luyện

--Tổng số câu hỏi của Thử Thách
SELECT
    COUNT(HQC.MaCauHoi) AS TongSoCauHoiThuThach
FROM
    HoatDongHocTap HD
LEFT JOIN 
    HoatDong_CauHoi HQC ON HD.MaHoatDong = HQC.MaHoatDong
WHERE
    HD.MaLoai = 'LHD04'; -- LHD04: Thử Thách

--Tổng số câu hỏi của Lý Thuyết
SELECT
    COUNT(HQC.MaCauHoi) AS TongSoCauHoiLyThuyet
FROM
    HoatDongHocTap HD
LEFT JOIN 
    HoatDong_CauHoi HQC ON HD.MaHoatDong = HQC.MaHoatDong
WHERE
    HD.MaLoai = 'LHD01'; -- LHD01: Lý Thuyết

--Tổng số câu hỏi của Trò Chơi
SELECT
    COUNT(HQC.MaCauHoi) AS TongSoCauHoiTroChoi
FROM
    HoatDongHocTap HD
LEFT JOIN 
    HoatDong_CauHoi HQC ON HD.MaHoatDong = HQC.MaHoatDong
WHERE
    HD.MaLoai = 'LHD05'; -- LHD05: Trò Chơi

---Câu hỏi thuộc loại Củng Cố (LHD02)
SELECT
    LHD.TenLoai,
    HD.TieuDe AS TenHoatDong,
    CH.MaCauHoi,
    CH.NoiDungCauHoi,
    CH.GiaiThich,
    DAP.NoiDungDapAn,
    DAP.LaDapAnDung
FROM
    LoaiHoatDong LHD
JOIN
    HoatDongHocTap HD ON LHD.MaLoai = HD.MaLoai
JOIN
    HoatDong_CauHoi HQC ON HD.MaHoatDong = HQC.MaHoatDong
JOIN
    CauHoi CH ON HQC.MaCauHoi = CH.MaCauHoi
JOIN
    DapAn DAP ON CH.MaCauHoi = DAP.MaCauHoi
WHERE
    LHD.MaLoai = 'LHD02' -- Củng Cố
ORDER BY
    HD.MaHoatDong, HQC.ThuTu, DAP.MaDapAn;

--Câu hỏi thuộc loại Ôn Luyện (LHD03)
SELECT
    LHD.TenLoai,
    HD.TieuDe AS TenHoatDong,
    CH.MaCauHoi,
    CH.NoiDungCauHoi,
    CH.GiaiThich,
    DAP.NoiDungDapAn,
    DAP.LaDapAnDung
FROM
    LoaiHoatDong LHD
JOIN
    HoatDongHocTap HD ON LHD.MaLoai = HD.MaLoai
JOIN
    HoatDong_CauHoi HQC ON HD.MaHoatDong = HQC.MaHoatDong
JOIN
    CauHoi CH ON HQC.MaCauHoi = CH.MaCauHoi
JOIN
    DapAn DAP ON CH.MaCauHoi = DAP.MaCauHoi
WHERE
    LHD.MaLoai = 'LHD03' -- Ôn Luyện
ORDER BY
    HD.MaHoatDong, HQC.ThuTu, DAP.MaDapAn;

--Câu hỏi thuộc loại Thử Thách (LHD04)
SELECT
    LHD.TenLoai,
    HD.TieuDe AS TenHoatDong,
    CH.MaCauHoi,
    CH.NoiDungCauHoi,
    CH.GiaiThich,
    DAP.NoiDungDapAn,
    DAP.LaDapAnDung
FROM
    LoaiHoatDong LHD
JOIN
    HoatDongHocTap HD ON LHD.MaLoai = HD.MaLoai
JOIN
    HoatDong_CauHoi HQC ON HD.MaHoatDong = HQC.MaHoatDong
JOIN
    CauHoi CH ON HQC.MaCauHoi = CH.MaCauHoi
JOIN
    DapAn DAP ON CH.MaCauHoi = DAP.MaCauHoi
WHERE
    LHD.MaLoai = 'LHD04' -- Thử Thách
ORDER BY
    HD.MaHoatDong, HQC.ThuTu, DAP.MaDapAn;

--Câu hỏi thuộc loại Lý Thuyết (LHD01)
SELECT
    LHD.TenLoai,
    HD.TieuDe AS TenHoatDong,
    CH.MaCauHoi,
    CH.NoiDungCauHoi,
    CH.GiaiThich,
    DAP.NoiDungDapAn,
    DAP.LaDapAnDung
FROM
    LoaiHoatDong LHD
JOIN
    HoatDongHocTap HD ON LHD.MaLoai = HD.MaLoai
LEFT JOIN -- Dùng LEFT JOIN để vẫn hiển thị Lý Thuyết dù không có câu hỏi
    HoatDong_CauHoi HQC ON HD.MaHoatDong = HQC.MaHoatDong
LEFT JOIN
    CauHoi CH ON HQC.MaCauHoi = CH.MaCauHoi
LEFT JOIN
    DapAn DAP ON CH.MaCauHoi = DAP.MaCauHoi
WHERE
    LHD.MaLoai = 'LHD01' -- Lý Thuyết
ORDER BY
    HD.MaHoatDong, HQC.ThuTu, DAP.MaDapAn;

--


--Tổng Hợp Số Lượng Câu Hỏi Đã Hoàn Thành Của Người Dùng
SELECT
    LHD.TenLoai,
    SUM(TT.SoCauDaLam) AS TongCauDaLam,
    SUM(TT.SoCauDung) AS TongCauDung,
    CAST(SUM(TT.DiemDatDuoc) AS INT) AS TongDiemKiemTra
FROM
    TienTrinhHocTap TT
JOIN
    HoatDongHocTap HD ON TT.MaHoatDong = HD.MaHoatDong
JOIN
    LoaiHoatDong LHD ON HD.MaLoai = LHD.MaLoai
WHERE
    TT.MaNguoiDung = 'ND002' -- Thay 'ND002' bằng người dùng hiện tại
GROUP BY
    LHD.TenLoai
ORDER BY
    LHD.TenLoai;

--Lấy Tỷ Lệ Hoàn Thành Bài Học Theo Môn Học Cho Người Dùng
WITH TongBaiHoc AS (
    -- Tổng số hoạt động (bài học) có thể làm
    SELECT
        MH.TenMonHoc,
        COUNT(HD.MaHoatDong) AS TongSoHoatDong
    FROM
        MonHoc MH
    JOIN
        HoatDongHocTap HD ON MH.MaMonHoc = HD.MaMonHoc
    GROUP BY
        MH.TenMonHoc
),
BaiDaHoanThanh AS (
    -- Số hoạt động đã hoàn thành bởi người dùng
    SELECT
        MH.TenMonHoc,
        COUNT(DISTINCT TT.MaHoatDong) AS SoBaiDaHoanThanh
    FROM
        TienTrinhHocTap TT
    JOIN
        HoatDongHocTap HD ON TT.MaHoatDong = HD.MaHoatDong
    JOIN
        MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
    WHERE
        TT.MaNguoiDung = 'ND004' -- Thay 'ND004' bằng người dùng hiện tại
        AND TT.DaHoanThanh = 1
    GROUP BY
        MH.TenMonHoc
)
SELECT
    TBH.TenMonHoc,
    ISNULL(BDA.SoBaiDaHoanThanh, 0) AS SoBaiDaHoanThanh,
    TBH.TongSoHoatDong,
    CAST(ISNULL(BDA.SoBaiDaHoanThanh, 0) * 100.0 / TBH.TongSoHoatDong AS DECIMAL(5, 2)) AS TiLePhanTramHoanThanh
FROM
    TongBaiHoc TBH
LEFT JOIN
    BaiDaHoanThanh BDA ON TBH.TenMonHoc = BDA.TenMonHoc;

--==================================================CHỨC NĂNG FIGMA===============================
-- 1. Đăng nhập: Kiểm tra thông tin
SELECT MaNguoiDung, MatKhauMaHoa, TongDiem
FROM NguoiDung
WHERE TenDangNhap = N'minhanh' OR Email = N'minhanh@gmail.com';

-- 2. Cập nhật lần đăng nhập cuối và tăng số lần trực tuyến
UPDATE NguoiDung
SET LanDangNhapCuoi = SYSUTCDATETIME(),
    SoLanTrucTuyen = SoLanTrucTuyen + 1
WHERE MaNguoiDung = 'ND002';

-- 3. Lấy 10 bài Lý thuyết cơ bản của Toán và Tiếng Việt.
SELECT TOP 10
    HD.MaHoatDong,
    HD.TieuDe,
    HD.MoTa,
    MH.TenMonHoc
FROM
    HoatDongHocTap HD
JOIN
    MonHoc MH ON HD.MaMonHoc = MH.MaMonHoc
WHERE
    HD.MaLoai = 'LHD01' -- Lý Thuyết
ORDER BY
    MH.TenMonHoc, HD.MaHoatDong; -- Sắp xếp để dễ nhìn

-- 4. Lấy chi tiết nội dung của 1 bài lý thuyết (VD: LT001 - Phép cộng).
SELECT TieuDe, MoTa AS NoiDungBaiHoc
FROM HoatDongHocTap
WHERE MaHoatDong = 'LT001';

-- 5. Lấy 10 câu hỏi của hoạt động Củng cố cụ thể (VD: CC001) theo thứ tự.
SELECT
    CH.MaCauHoi,
    CH.NoiDungCauHoi,
    DAP.MaDapAn,
    DAP.NoiDungDapAn,
    HQC.ThuTu
FROM
    HoatDong_CauHoi HQC
JOIN
    CauHoi CH ON HQC.MaCauHoi = CH.MaCauHoi
JOIN
    DapAn DAP ON CH.MaCauHoi = DAP.MaCauHoi
WHERE
    HQC.MaHoatDong = 'CC001' -- Lấy câu hỏi của Củng cố Toán 1
ORDER BY
    HQC.ThuTu, DAP.MaDapAn;

-- 6. Lấy 10 câu hỏi của hoạt động Ôn Luyện (VD: OL001). Cần XÁO TRỘN trong code logic.
-- Truy vấn này lấy ra câu hỏi và đáp án. Sau đó, code ứng dụng sẽ xáo trộn thứ tự câu hỏi và đáp án.
SELECT
    CH.MaCauHoi,
    CH.NoiDungCauHoi,
    DAP.MaDapAn,
    DAP.NoiDungDapAn,
    DAP.LaDapAnDung
FROM
    HoatDong_CauHoi HQC
JOIN
    CauHoi CH ON HQC.MaCauHoi = CH.MaCauHoi
JOIN
    DapAn DAP ON CH.MaCauHoi = DAP.MaCauHoi
WHERE
    HQC.MaHoatDong = 'OL001' -- Lấy câu hỏi của Ôn Luyện 1
ORDER BY
    NEWID(); -- Dùng NEWID() để trả về kết quả ngẫu nhiên (xáo trộn) trong SQL

-- 7 Lấy 20 câu hỏi của Thử Thách (TT001) để người dùng làm.
SELECT
    CH.MaCauHoi,
    CH.NoiDungCauHoi,
    DAP.NoiDungDapAn -- Chỉ cần lấy nội dung để hiển thị trắc nghiệm
FROM
    HoatDong_CauHoi HQC
JOIN
    CauHoi CH ON HQC.MaCauHoi = CH.MaCauHoi
JOIN
    DapAn DAP ON CH.MaCauHoi = DAP.MaCauHoi
WHERE
    HQC.MaHoatDong = 'TT001' -- Lấy câu hỏi của Siêu Thử Thách
ORDER BY
    HQC.ThuTu, DAP.MaDapAn;

-- 8. Lấy TỔNG ĐIỂM và phân loại Điểm Kiểm Tra / Điểm Hàng Ngày (theo yêu cầu giao diện)
SELECT
    (SELECT SUM(SoDiem) FROM LichSuHoatDong WHERE MaNguoiDung = 'ND002') AS TongDiem,
    ISNULL(SUM(CASE WHEN LoaiDiem = N'Kiểm tra' THEN SoDiem ELSE 0 END), 0) AS DiemKiemTra,
    ISNULL(SUM(CASE WHEN LoaiDiem = N'Hàng ngày' OR LoaiDiem = N'Streaks' THEN SoDiem ELSE 0 END), 0) AS DiemHangNgay
FROM
    LichSuHoatDong
WHERE
    MaNguoiDung = 'ND002';


--9. Lấy chi tiết lịch sử điểm (như trong ảnh giao diện)
SELECT
    SoDiem,
    ThoiGian,
    ChiTiet
FROM
    LichSuHoatDong
WHERE
    MaNguoiDung = 'ND002'
ORDER BY
    ThoiGian DESC;

-- 10. Lấy TOP 5 người dùng có điểm cao nhất
SELECT TOP 5
    ROW_NUMBER() OVER (ORDER BY TongDiem DESC) AS Hang,
    TenDangNhap,
    TongDiem
FROM
    NguoiDung
ORDER BY
    TongDiem DESC;

-- GIẢ ĐỊNH: Người dùng ND002 làm xong bài 'CC003' và được 45 điểm
-- Bước 1: Ghi nhận điểm vào Lịch sử hoạt động
-- làm gì: Chèn một bản ghi mới vào lịch sử điểm.
INSERT INTO LichSuHoatDong (MaLichSu, MaNguoiDung, LoaiDiem, SoDiem, ChiTiet)
VALUES (
    'LS000001', -- Cần logic tạo ID duy nhất
    'ND002',
    N'Kiểm tra',
    45,
    N'Hoàn thành Củng cố Toán 3'
);

-- Bước 2: Cập nhật lại Tổng Điểm trong bảng NguoiDung
-- làm gì: Cộng điểm mới vào TongDiem của người dùng.
UPDATE NguoiDung
SET TongDiem = TongDiem + 45
WHERE MaNguoiDung = 'ND002';