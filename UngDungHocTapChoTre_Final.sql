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
    MaCauHoi char(6) PRIMARY KEY,
    NoiDungCauHoi NVARCHAR(300) NOT NULL,
    GiaiThich NVARCHAR(300) NULL,
    DiemToiDa INT NOT NULL -- Điểm tối đa cho 1 câu hỏi
);
GO

-- 6. Bảng DapAn (Answer)
CREATE TABLE DapAn (
    MaDapAn char(6) PRIMARY KEY,
    MaCauHoi char(6) FOREIGN KEY REFERENCES CauHoi(MaCauHoi),
    NoiDungDapAn NVARCHAR(100) NOT NULL,
    LaDapAnDung BIT NOT NULL -- Sử dụng BIT (1=Đúng, 0=Sai)
);
GO

-- 7. Bảng HoatDong_CauHoi (M:N link giữa Hoạt động và Câu hỏi)
CREATE TABLE HoatDong_CauHoi (
    MaHoatDong char(5) FOREIGN KEY REFERENCES HoatDongHocTap(MaHoatDong),
    MaCauHoi char(6) FOREIGN KEY REFERENCES CauHoi(MaCauHoi),
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


---------------------------------------------------------------------
-- DML: INSERT DỮ LIỆU
---------------------------------------------------------------------

-- 1) NguoiDung
INSERT INTO NguoiDung (MaNguoiDung, TenDangNhap, Email, MatKhauMaHoa, TongDiem) VALUES
('ND001', N'huy', N'phamminhhuy0901tk@gmail.com', N'123', 0),
('ND002', N'minhanh', N'minhanh@gmail.com', N'hash', 55),
('ND003', N'tuan', N'tuan@gmail.com', N'hash', 50),
('ND004', N'duyquoc', N'leduyquoc123meo@gmail.com', N'123', 140),
('ND005', N'khang', N'khangheheqt@gmail.com', N'123', 35),
('ND006', N'huynh', N'huynh746926@gmail.com', N'123', 27);
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
('OL004', @MaMH_Toan, @MaLoai_OL, N'Ôn Cơ bản 4', N'Bài tập ôn luyện cơ bản', 50),
('OL005', @MaMH_Toan, @MaLoai_OL, N'Ôn Cơ bản 5', N'Bài tập ôn luyện cơ bản', 50),
('OL006', @MaMH_Toan, @MaLoai_OL, N'Ôn Cơ bản 6', N'Bài tập ôn luyện cơ bản', 50),
('OL007', @MaMH_Toan, @MaLoai_OL, N'Ôn Cơ bản 7', N'Bài tập ôn luyện cơ bản', 50),
('OL008', @MaMH_Toan, @MaLoai_OL, N'Ôn Cơ bản 8', N'Bài tập ôn luyện cơ bản', 50),
('OL009', @MaMH_Toan, @MaLoai_OL, N'Ôn Cơ bản 9', N'Bài tập ôn luyện cơ bản', 50),
('OL010', @MaMH_Toan, @MaLoai_OL, N'Ôn Cơ bản 10',N'Bài tập ôn luyện cơ bản', 50),
('OL011', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 1', N'Bài tập ôn luyện trung bình', 80),
('OL012', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 2', N'Bài tập ôn luyện trung bình', 80),
('OL013', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 3', N'Bài tập ôn luyện trung bình', 80),
('OL014', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 4', N'Bài tập ôn luyện trung bình', 80),
('OL015', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 5', N'Bài tập ôn luyện trung bình', 80),
('OL016', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 6', N'Bài tập ôn luyện trung bình', 80),
('OL017', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 7', N'Bài tập ôn luyện trung bình', 80),
('OL018', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 8', N'Bài tập ôn luyện trung bình', 80),
('OL019', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 9', N'Bài tập ôn luyện trung bình', 80),
('OL020', @MaMH_Toan, @MaLoai_OL, N'Ôn TB 10',N'Bài tập ôn luyện trung bình', 80),
('OL021', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 1', N'Bài tập ôn luyện nâng cao', 100),
('OL022', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 2', N'Bài tập ôn luyện nâng cao', 100),
('OL023', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 3', N'Bài tập ôn luyện nâng cao', 100),
('OL024', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 4', N'Bài tập ôn luyện nâng cao', 100),
('OL025', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 5', N'Bài tập ôn luyện nâng cao', 100),
('OL026', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 6', N'Bài tập ôn luyện nâng cao', 100),
('OL027', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 7', N'Bài tập ôn luyện nâng cao', 100),
('OL028', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 8', N'Bài tập ôn luyện nâng cao', 100),
('OL029', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 9', N'Bài tập ôn luyện nâng cao', 100),
('OL030', @MaMH_Toan, @MaLoai_OL, N'Ôn NC 10', N'Bài tập ôn luyện nâng cao', 100);

-- d. ThuThach (TT001) - Gán tạm cho Toán
INSERT INTO HoatDongHocTap (MaHoatDong, MaMonHoc, MaLoai, TieuDe, MoTa, TongDiemToiDa) VALUES
('TT001', @MaMH_Toan, @MaLoai_TT, N'Siêu Thử Thách', N'20 câu khó nhất!', 100),
('TT002', @MaMH_Toan, @MaLoai_TT, N'Thử Thách Đã Học', N'20 câu hỏi đã học!', 100);
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



-- 10. Lấy TOP 5 người dùng có điểm cao nhất
SELECT TOP 5
    ROW_NUMBER() OVER (ORDER BY TongDiem DESC) AS Hang,
    TenDangNhap,
    TongDiem
FROM
    NguoiDung
ORDER BY
    TongDiem DESC;

-- Bước 2: Cập nhật lại Tổng Điểm trong bảng NguoiDung
-- làm gì: Cộng điểm mới vào TongDiem của người dùng.
UPDATE NguoiDung
SET TongDiem = TongDiem + 45
WHERE MaNguoiDung = 'ND002';

use UngDungHocTapChoTre
go 
select * from NguoiDung
GO


USE UngDungHocTapChoTre;
GO

-- Xóa mapping để không bị vướng FK khác
DELETE FROM HoatDong_CauHoi;
-- Xóa tất cả đáp án
DELETE FROM DapAn;
-- Xóa tất cả câu hỏi
DELETE FROM CauHoi;
GO
-- Kiểm tra số lượng câu hỏi sau khi xóa
SELECT COUNT(*) AS SoCauHoi FROM CauHoi;
SELECT COUNT(*) AS SoDapAn FROM DapAn;
USE UngDungHocTapChoTre;
GO

-- Xoá câu hỏi cũ (nếu cần)
-- DELETE FROM CauHoi;
-- GO

INSERT INTO CauHoi (MaCauHoi, NoiDungCauHoi, GiaiThich, DiemToiDa)
VALUES
-- =========================================================
-- 1) CỦNG CỐ TOÁN: 100 câu (CH001–CH100)
-- Bài 1 (CH001–CH010)
('CH001', N'2 + 3 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH002', N'5 - 1 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH003', N'4 + 4 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH004', N'9 - 3 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH005', N'7 + 1 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH006', N'12 - 5 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH007', N'8 + 6 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH008', N'13 - 4 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH009', N'15 - 7 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH010', N'9 + 8 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),

-- Bài 2 (CH011–CH020)
('CH011', N'3 + 6 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH012', N'10 - 2 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH013', N'1 + 8 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH014', N'6 - 4 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH015', N'7 + 3 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH016', N'14 - 6 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH017', N'11 + 5 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH018', N'12 - 3 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH019', N'16 - 9 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH020', N'9 + 7 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),

-- Bài 3 (CH021–CH030)
('CH021', N'5 + 2 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH022', N'9 - 1 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH023', N'4 + 5 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH024', N'8 - 2 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH025', N'3 + 7 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH026', N'11 - 4 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH027', N'6 + 9 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH028', N'13 - 6 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH029', N'17 - 8 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH030', N'9 + 9 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),

-- Bài 4 (CH031–CH040)
('CH031', N'4 + 6 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH032', N'7 - 2 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH033', N'2 + 9 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH034', N'10 - 5 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH035', N'3 + 4 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH036', N'15 - 6 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH037', N'8 + 7 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH038', N'14 - 5 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH039', N'18 - 9 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH040', N'11 + 8 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),

-- Bài 5 (CH041–CH050)
('CH041', N'5 + 4 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH042', N'6 - 1 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH043', N'9 + 2 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH044', N'7 - 3 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH045', N'3 + 8 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH046', N'12 - 4 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH047', N'8 + 5 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH048', N'14 - 7 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH049', N'16 - 8 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH050', N'7 + 9 = ?', N'Đây là phép tính cộng/trừ đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),

-- Bài 6 (CH051–CH060) – có nhân/chia
('CH051', N'6 × 2 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH052', N'12 ÷ 3 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH053', N'7 + 12 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH054', N'20 - 9 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH055', N'5 × 3 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH056', N'18 ÷ 2 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH057', N'14 + 15 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH058', N'28 - 13 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH059', N'9 × 4 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH060', N'36 ÷ 6 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),

-- Bài 7 (CH061–CH070)
('CH061', N'4 × 3 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH062', N'16 ÷ 4 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH063', N'11 + 8 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH064', N'25 - 14 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH065', N'3 × 6 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH066', N'21 ÷ 3 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH067', N'17 + 12 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH068', N'32 - 17 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH069', N'8 × 5 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH070', N'42 ÷ 7 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),

-- Bài 8 (CH071–CH080)
('CH071', N'7 × 3 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH072', N'18 ÷ 2 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH073', N'14 + 9 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH074', N'29 - 16 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH075', N'9 × 2 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH076', N'30 ÷ 5 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH077', N'13 + 17 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH078', N'33 - 14 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH079', N'6 × 7 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH080', N'48 ÷ 8 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),

-- Bài 9 (CH081–CH090)
('CH081', N'8 × 3 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH082', N'20 ÷ 4 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH083', N'15 + 13 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH084', N'27 - 15 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH085', N'5 × 4 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH086', N'24 ÷ 6 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH087', N'18 + 11 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH088', N'35 - 19 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH089', N'9 × 5 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH090', N'45 ÷ 9 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),

-- Bài 10 (CH091–CH100)
('CH091', N'6 × 4 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH092', N'28 ÷ 4 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH093', N'19 + 12 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH094', N'31 - 18 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH095', N'7 × 3 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH096', N'32 ÷ 8 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH097', N'21 + 17 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH098', N'40 - 23 = ?', N'Đây là phép tính cộng/trừ. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH099', N'8 × 6 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),
('CH100', N'54 ÷ 6 = ?', N'Đây là phép nhân/chia đơn giản. Con hãy tính rồi chọn đáp án đúng.', 5),

-- =========================================================
-- 2) CỦNG CỐ TIẾNG VIỆT: 100 câu (CH101–CH200)
-- Dạng 1: Danh từ (CH101–CH120)
('CH101', N'Từ nào là danh từ? (bé, chạy, đẹp, nhanh)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH102', N'Từ nào là danh từ? (mèo, cao, ăn, đỏ)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH103', N'Từ nào là danh từ? (cái bàn, ngủ, đẹp, to)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH104', N'Từ nào là danh từ? (quả cam, viết, đẹp, mệt)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH105', N'Từ nào là danh từ? (bố, nhỏ, nhảy, vàng)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH106', N'Từ nào là danh từ? (con chó, xanh, đi, cao)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH107', N'Từ nào là danh từ? (ngôi nhà, buồn, ăn, thấp)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH108', N'Từ nào là danh từ? (cái ghế, chạy, vui, to)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH109', N'Từ nào là danh từ? (quyển sách, ngủ, đỏ, đẹp)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH110', N'Từ nào là danh từ? (con gà, nhanh, vui, to)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH111', N'Từ nào là danh từ? (bạn Nam, hát, cao, buồn)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH112', N'Từ nào là danh từ? (cái bóng, vui, ăn, sạch)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH113', N'Từ nào là danh từ? (mẹ, chạy, xanh, nhỏ)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH114', N'Từ nào là danh từ? (cha, vui, to, đi)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH115', N'Từ nào là danh từ? (em bé, xinh, ăn, ngồi)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH116', N'Từ nào là danh từ? (quả táo, đỏ, mở, nhanh)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH117', N'Từ nào là danh từ? (bạn Lan, cao, chạy, mệt)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH118', N'Từ nào là danh từ? (bạn nhỏ, buồn, khóc, xinh)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH119', N'Từ nào là danh từ? (cái nón, vàng, ăn, lạnh)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH120', N'Từ nào là danh từ? (mặt trời, sáng, vui, thấp)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),

-- Dạng 2: Động từ (CH121–CH140)
('CH121', N'Từ nào là động từ? (ăn, đỏ, bé, đẹp)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH122', N'Từ nào là động từ? (nhảy, cao, mèo, vàng)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH123', N'Từ nào là động từ? (chạy, xanh, vui, nhỏ)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH124', N'Từ nào là động từ? (ngủ, to, bàn, mệt)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH125', N'Từ nào là động từ? (hát, thấp, sách, vàng)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH126', N'Từ nào là động từ? (đọc, buồn, ghế, ấm)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH127', N'Từ nào là động từ? (đi, bé, cây, cao)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH128', N'Từ nào là động từ? (cười, to, mèo, đẹp)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH129', N'Từ nào là động từ? (nhìn, xanh, táo, vui)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH130', N'Từ nào là động từ? (bay, nhỏ, bút, đỏ)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH131', N'Từ nào là động từ? (viết, cao, nước, lạnh)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH132', N'Từ nào là động từ? (ngồi, to, bông hoa, sạch)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH133', N'Từ nào là động từ? (bơi, mệt, con chó, vàng)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH134', N'Từ nào là động từ? (mở, xanh, cái ghế, buồn)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH135', N'Từ nào là động từ? (đẩy, to, đồ chơi, vui)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH136', N'Từ nào là động từ? (nấu, nóng, sách, cao)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH137', N'Từ nào là động từ? (uống, vàng, bút chì, mặn)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH138', N'Từ nào là động từ? (học, ấm, đàn, xanh)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH139', N'Từ nào là động từ? (vẽ, mỏng, quả cam, cao)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH140', N'Từ nào là động từ? (leo, buồn, con mèo, sạch)', N'Động từ là từ chỉ hoạt động, việc làm.', 5),

-- Dạng 3: Tính từ (CH141–CH160)
('CH141', N'Từ nào là tính từ? (cao, mèo, chạy, uống)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH142', N'Từ nào là tính từ? (đẹp, cái bàn, đi, ăn)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH143', N'Từ nào là tính từ? (vui, sách, uống, chạy)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH144', N'Từ nào là tính từ? (buồn, con chó, ngồi, xanh)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH145', N'Từ nào là tính từ? (xanh, ghế, ăn, đá)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH146', N'Từ nào là tính từ? (ấm, mèo, chạy, to)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH147', N'Từ nào là tính từ? (lạnh, em bé, viết, nấu)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH148', N'Từ nào là tính từ? (mệt, cái thước, cười, đọc)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH149', N'Từ nào là tính từ? (nhanh, bé, ngồi, hát)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH150', N'Từ nào là tính từ? (sạch, bàn, ăn, uống)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH151', N'Từ nào là tính từ? (vàng, mèo, mở, nấu)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH152', N'Từ nào là tính từ? (thấp, sách, đi, hát)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH153', N'Từ nào là tính từ? (to, bút, ăn, ngồi)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH154', N'Từ nào là tính từ? (nhỏ, bé, chạy, mở)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH155', N'Từ nào là tính từ? (hiền, con chó, uống, bay)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH156', N'Từ nào là tính từ? (ngọt, quả táo, ngồi, nhảy)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH157', N'Từ nào là tính từ? (thơm, hoa, nhìn, hát)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH158', N'Từ nào là tính từ? (trẻ, học sinh, viết, đi)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH159', N'Từ nào là tính từ? (dài, cái thước, đọc, khóc)', N'Tính từ là từ chỉ đặc điểm.', 5),
('CH160', N'Từ nào là tính từ? (cao lớn, bạn Nam, ngủ, nhảy)', N'Tính từ là từ chỉ đặc điểm.', 5),

-- Dạng 4: Chính tả (CH161–CH180)
('CH161', N'Từ nào viết đúng? (nghỉ, nghi~, nghiỉ, nghị~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH162', N'Từ nào viết đúng? (ngọt, ngo~t, ngot~, ngọt~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH163', N'Từ nào viết đúng? (trượt, chượt, truột, chuột~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH164', N'Từ nào viết đúng? (thuyền, thuỳen, thuyên~, thuyển~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH165', N'Từ nào viết đúng? (giờ, giờ~, gờ, giơ~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH166', N'Từ nào viết đúng? (bún, búnn, bủn, bùn~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH167', N'Từ nào viết đúng? (mảnh, mãnh~, mánh, manh~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH168', N'Từ nào viết đúng? (gạo, gạoo, gảo, gao~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH169', N'Từ nào viết đúng? (lạnh, lanh~, lãnh~, lảnh~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH170', N'Từ nào viết đúng? (nghiêng, nghiêng~, ngiêng, nghieng)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH171', N'Từ nào viết đúng? (mèo, meò, mèo~, meo~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH172', N'Từ nào viết đúng? (mũi, mũii, mùi~, mui~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH173', N'Từ nào viết đúng? (đẹp, đẹpp, dẹp, đẹp~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH174', N'Từ nào viết đúng? (vẽ, vẻ~, ve~, vẽ~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH175', N'Từ nào viết đúng? (gió, giáo~, gió~, gio~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH176', N'Từ nào viết đúng? (cánh, cáng~, cãnh~, cánh~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH177', N'Từ nào viết đúng? (bút, bụt, but~, bút~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH178', N'Từ nào viết đúng? (mưa, mưa~, mư~, mưa~)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH179', N'Từ nào viết đúng? (sữa, sưã, sữa~, su~a)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH180', N'Từ nào viết đúng? (bàn, bằn, bàn~, ban~)', N'Hãy chọn từ viết đúng chính tả.', 5),

-- Dạng 5: Hoàn thiện câu (CH181–CH200)
('CH181', N'Bé đang ___ bóng.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH182', N'Con mèo ___ trên ghế.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH183', N'Bạn Nam rất ___ khi được khen.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH184', N'Mẹ mua cho em một ___ mới.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH185', N'Em bé đang ___ sữa.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH186', N'Bố đi làm bằng ___.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH187', N'Hôm nay trời rất ___.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH188', N'Cô giáo đang ___ bài.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH189', N'Bạn nhỏ đang ___ tranh.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH190', N'Con chó chạy ___ sân.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH191', N'Mẹ bảo em phải ___ tay trước khi ăn.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH192', N'Bé Lan đang ___ sách.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH193', N'Ông nội đang ___ báo.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH194', N'Chị gái em rất ___.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH195', N'Em đi học mang theo một chiếc ___.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH196', N'Bạn nhỏ chơi ___ cùng bạn.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH197', N'Bé cười rất ___.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH198', N'Em đặt cặp lên ___.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH199', N'Bạn nhỏ đang ___ bánh.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),
('CH200', N'Mẹ đang chuẩn bị ___ cho cả nhà.', N'Hãy chọn từ thích hợp để hoàn thành câu.', 5),

-- =========================================================
-- 3) ÔN LUYỆN: 90 câu (CH201–CH290)
-- Mức Cơ bản (OL001–OL003): CH201–CH230
('CH201', N'7 + 4 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH202', N'15 - 6 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH203', N'3 × 3 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH204', N'16 ÷ 4 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH205', N'12 + 5 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH206', N'Từ nào là danh từ? (bàn, chạy, đỏ, cao)', N'Câu ôn luyện TV mức cơ bản: danh từ.', 5),
('CH207', N'Từ nào là động từ? (ăn, mèo, đẹp, bé)', N'Câu ôn luyện TV mức cơ bản: động từ.', 5),
('CH208', N'Hoàn thiện câu: "Bé đang ___ sách."', N'Câu ôn luyện TV mức cơ bản: hoàn thiện câu.', 5),
('CH209', N'Từ nào viết đúng? (ngọt, ngoạt~, ngọt~, ngọt~)', N'Câu ôn luyện TV mức cơ bản: chính tả.', 5),
('CH210', N'Từ nào là tính từ? (xanh, chạy, ăn, ghế)', N'Câu ôn luyện TV mức cơ bản: tính từ.', 5),

('CH211', N'18 - 9 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH212', N'9 + 7 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH213', N'4 × 5 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH214', N'20 ÷ 5 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH215', N'13 + 11 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH216', N'Từ nào viết đúng? (mưa, mư~, mu~a, mưa~)', N'Câu ôn luyện TV mức cơ bản: chính tả.', 5),
('CH217', N'Chọn động từ: (nhảy, đẹp, bàn, vàng)', N'Câu ôn luyện TV mức cơ bản: động từ.', 5),
('CH218', N'Hoàn thiện câu: "Con chó đang ___ xương."', N'Câu ôn luyện TV mức cơ bản: hoàn thiện câu.', 5),
('CH219', N'Danh từ là? (cái ghế, đỏ, chạy, mệt)', N'Câu ôn luyện TV mức cơ bản: danh từ.', 5),
('CH220', N'Tính từ là? (buồn, chạy, ăn, mèo)', N'Câu ôn luyện TV mức cơ bản: tính từ.', 5),

('CH221', N'25 - 8 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH222', N'14 + 6 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH223', N'6 × 3 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH224', N'30 ÷ 6 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH225', N'17 + 12 = ?', N'Câu ôn luyện Toán mức cơ bản.', 5),
('CH226', N'Từ nào là động từ? (viết, đỏ, bé, vàng)', N'Câu ôn luyện TV mức cơ bản: động từ.', 5),
('CH227', N'Từ nào là danh từ? (con mèo, xanh, ăn, vui)', N'Câu ôn luyện TV mức cơ bản: danh từ.', 5),
('CH228', N'Hoàn thiện câu: "Mẹ đang ___ cơm."', N'Câu ôn luyện TV mức cơ bản: hoàn thiện câu.', 5),
('CH229', N'Từ nào viết đúng? (bút, but~, bụt, búc)', N'Câu ôn luyện TV mức cơ bản: chính tả.', 5),
('CH230', N'Tính từ là? (dài, chạy, ăn, ngủ)', N'Câu ôn luyện TV mức cơ bản: tính từ.', 5),

-- Mức Trung bình (OL004–OL006): CH231–CH260
('CH231', N'28 - 13 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH232', N'19 + 15 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH233', N'7 × 4 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH234', N'40 ÷ 5 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH235', N'26 - 9 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH236', N'Hoàn thiện câu: "Bạn nhỏ rất ___ khi được khen."', N'Câu ôn luyện TV mức trung bình.', 5),
('CH237', N'Chọn động từ: (học, đẹp, mèo, xanh)', N'Câu ôn luyện TV mức trung bình.', 5),
('CH238', N'Từ nào viết đúng? (vẽ, vẻ~, vẽ~, ve~)', N'Câu ôn luyện TV mức trung bình: chính tả.', 5),
('CH239', N'Danh từ là? (cái nón, chạy, to, nhanh)', N'Câu ôn luyện TV mức trung bình: danh từ.', 5),
('CH240', N'Tính từ là? (ấm, chạy, viết, mèo)', N'Câu ôn luyện TV mức trung bình: tính từ.', 5),

('CH241', N'36 - 17 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH242', N'18 + 16 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH243', N'8 × 6 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH244', N'45 ÷ 9 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH245', N'33 - 14 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH246', N'Hoàn thiện câu: "Quyển vở có màu ___."', N'Câu ôn luyện TV mức trung bình.', 5),
('CH247', N'Từ nào là động từ? (ngồi, bàn, xanh, vàng)', N'Câu ôn luyện TV mức trung bình: động từ.', 5),
('CH248', N'Tính từ là? (nhỏ, mèo, ăn, uống)', N'Câu ôn luyện TV mức trung bình: tính từ.', 5),
('CH249', N'Danh từ? (quả táo, đỏ, chạy, mệt)', N'Câu ôn luyện TV mức trung bình: danh từ.', 5),
('CH250', N'Từ nào viết đúng? (mũi, mu~i, mũii, mủi)', N'Câu ôn luyện TV mức trung bình: chính tả.', 5),

('CH251', N'42 - 18 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH252', N'27 + 19 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH253', N'9 × 7 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH254', N'50 ÷ 10 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH255', N'38 - 21 = ?', N'Câu ôn luyện Toán mức trung bình.', 5),
('CH256', N'Hoàn thiện câu: "Bé Lan đang ___ bài."', N'Câu ôn luyện TV mức trung bình.', 5),
('CH257', N'Danh từ là? (cái nhà, đẹp, ăn, vui)', N'Câu ôn luyện TV mức trung bình: danh từ.', 5),
('CH258', N'Động từ là? (mở, to, chó, đỏ)', N'Câu ôn luyện TV mức trung bình: động từ.', 5),
('CH259', N'Tính từ là? (thơm, mèo, mở, chạy)', N'Câu ôn luyện TV mức trung bình: tính từ.', 5),
('CH260', N'Từ nào viết đúng? (giờ, gio~, giơ~, giờ~)', N'Câu ôn luyện TV mức trung bình: chính tả.', 5),

-- Mức Nâng cao (OL007–OL009): CH261–CH290
('CH261', N'56 - 27 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH262', N'31 + 28 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH263', N'12 × 4 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH264', N'64 ÷ 8 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH265', N'45 - 23 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH266', N'Từ nào viết đúng? (cánh, cãnh~, cáng~, cánh~)', N'Câu ôn luyện TV mức nâng cao: chính tả.', 5),
('CH267', N'Tính từ là? (sáng, chạy, ăn, bàn)', N'Câu ôn luyện TV mức nâng cao: tính từ.', 5),
('CH268', N'Hoàn thiện câu: "Bố đang ___ xe."', N'Câu ôn luyện TV mức nâng cao.', 5),
('CH269', N'Động từ là? (bơi, mèo, to, đẹp)', N'Câu ôn luyện TV mức nâng cao: động từ.', 5),
('CH270', N'Danh từ là? (cái bút, xanh, vui, ngồi)', N'Câu ôn luyện TV mức nâng cao: danh từ.', 5),

('CH271', N'63 - 29 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH272', N'34 + 27 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH273', N'11 × 6 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH274', N'72 ÷ 9 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH275', N'58 - 19 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH276', N'Hoàn thiện câu: "Con mèo đang ___ bóng."', N'Câu ôn luyện TV mức nâng cao.', 5),
('CH277', N'Động từ là? (nhảy, ghế, to, cao)', N'Câu ôn luyện TV mức nâng cao: động từ.', 5),
('CH278', N'Tính từ là? (lạnh, mèo, mở, nhìn)', N'Câu ôn luyện TV mức nâng cao: tính từ.', 5),
('CH279', N'Danh từ là? (con chó, chạy, xanh, buồn)', N'Câu ôn luyện TV mức nâng cao: danh từ.', 5),
('CH280', N'Từ đúng chính tả? (lạnh, lảnh~, lanh~, lănh~)', N'Câu ôn luyện TV mức nâng cao: chính tả.', 5),

('CH281', N'72 - 34 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH282', N'43 + 26 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH283', N'13 × 7 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH284', N'81 ÷ 9 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH285', N'67 - 25 = ?', N'Câu ôn luyện Toán mức nâng cao.', 5),
('CH286', N'Hoàn thiện câu: "Bạn nhỏ đang ___ bánh."', N'Câu ôn luyện TV mức nâng cao.', 5),
('CH287', N'Từ nào là động từ? (leo, đẹp, mèo, xanh)', N'Câu ôn luyện TV mức nâng cao: động từ.', 5),
('CH288', N'Tính từ là? (sạch, con mèo, ngồi, chạy)', N'Câu ôn luyện TV mức nâng cao: tính từ.', 5),
('CH289', N'Danh từ là? (cái thước, vui, ăn, to)', N'Câu ôn luyện TV mức nâng cao: danh từ.', 5),
('CH290', N'Từ nào viết đúng? (bàn, ban~, bằn, bàn~)', N'Câu ôn luyện TV mức nâng cao: chính tả.', 5),

-- =========================================================
-- 4) THỬ THÁCH: 20 câu (CH291–CH310)
-- Toán thử thách (CH291–CH310)
('CH291', N'Hãy tính: 7 + 9 = ?', N'Cộng các số lại.', 5),
('CH292', N'Hãy tính: 18 ÷ 3 = ?', N'Chia đều cho 3.', 5),
('CH293', N'Hãy tính: 45 - 16 = ?', N'Thực hiện phép trừ.', 5),
('CH294', N'Hãy tính: 6 × 3 = ?', N'Thực hiện phép nhân.', 5),
('CH295', N'Hãy tính: 12 + 8 = ?', N'Cộng các số lại.', 5),
('CH296', N'Hãy tính: 20 - 6 = ?', N'Thực hiện phép trừ.', 5),
('CH297', N'Hãy tính: 9 × 4 = ?', N'Thực hiện phép nhân.', 5),
('CH298', N'Hãy tính: 50 ÷ 5 = ?', N'Thực hiện phép chia.', 5),
('CH299', N'Hãy tính: 15 + 9 = ?', N'Cộng các số lại.', 5),
('CH300', N'Hãy tính: 8 × 3 = ?', N'Thực hiện phép nhân.', 5),
('CH301', N'Hãy tính: 10 - 5 = ?', N'Tính phép trừ.', 5),
('CH302', N'Hãy tính: 36 ÷ 3 = ?', N'Thực hiện phép chia.', 5),
('CH303', N'Hãy tính: 27 - 9 = ?', N'Thực hiện phép trừ.', 5),
('CH304', N'Hãy tính: 7 × 5 = ?', N'Thực hiện phép nhân.', 5),
('CH305', N'Hãy tính: 32 ÷ 4 = ?', N'Thực hiện phép chia.', 5),
('CH306', N'Hãy tính: 14 + 6 = ?', N'Cộng hai số lại.', 5),
('CH307', N'Hãy tính: 18 + 12 = ?', N'Cộng các số lại.', 5),
('CH308', N'Hãy tính: 42 ÷ 7 = ?', N'Thực hiện phép chia.', 5),
('CH309', N'Hãy tính: 9 × 2 = ?', N'Thực hiện phép nhân.', 5),
('CH310', N'Hãy tính: 25 - 5 = ?', N'Thực hiện phép trừ.', 5),


-- TV thử thách (CH311–CH330)
('CH311', N'Trong câu "Bé Lan đang đọc sách", từ nào là động từ?', N'Động từ là từ chỉ hoạt động, việc làm.', 5),
('CH312', N'Chọn từ đúng để hoàn thành câu: "Bạn nhỏ rất ___ khi giúp đỡ mọi người." (vui, chạy, cao, đọc)', N'Hãy chọn từ chỉ cảm xúc phù hợp.', 5),
('CH313', N'Từ nào có nghĩa trái ngược với "mạnh"? (yếu, nhanh, đẹp, cao)', N'Từ trái nghĩa là từ có nghĩa ngược lại.', 5),
('CH314', N'Từ nào viết đúng chính tả? (khỏe, khoẻ~, khỏe~, khoẽ)', N'Hãy chọn từ viết đúng chính tả.', 5),
('CH315', N'Hoàn thành câu: "Hôm nay thời tiết thật ___, phù hợp để đi chơi." (đẹp, chạy, bé, bàn)', N'Hãy chọn tính từ phù hợp.', 5),
('CH316', N'Trong các từ sau, từ nào là danh từ? (con đường, cao, ăn, ngủ)', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH317', N'Trong câu "Mẹ đang chuẩn bị bữa tối", từ "chuẩn bị" thuộc loại nào?', N'Động từ là từ chỉ hoạt động.', 5),
('CH318', N'Hoàn thiện câu: "Bé Nam cố gắng ___ bài toán khó." (giải, đẹp, ăn, xanh)', N'Hãy chọn động từ phù hợp.', 5),
('CH319', N'Trong câu "Ông em đang tưới cây", từ nào là danh từ?', N'Danh từ là từ chỉ người, vật, sự vật.', 5),
('CH320', N'Chọn từ đúng để hoàn thành câu: "Bé Hoa rất ___ khi được điểm tốt." (vui, chạy, ăn, cao)', N'Hãy chọn từ chỉ cảm xúc.', 5),
('CH321', N'Từ nào có nghĩa trái ngược với "cao"? (thấp, đẹp, nhanh, nóng)', N'Hãy chọn từ có nghĩa ngược lại.', 5),
('CH322', N'Từ nào viết đúng chính tả? (nghỉ ngơi, nghỉ ngơi~, nghĩ ngơi, ngĩ ngơi)', N'Hãy chọn từ viết đúng.', 5),
('CH323', N'Hoàn thành câu: "Cả lớp đang ___ bài hát quốc ca." (hát, đỏ, vui, chạy)', N'Hãy chọn động từ phù hợp.', 5),
('CH324', N'Trong các từ sau, từ nào là tính từ? (đẹp, mèo, ăn, bút)', N'Tính từ là từ miêu tả đặc điểm.', 5),
('CH325', N'Trong câu "Bạn Nam rất chăm chỉ", từ "chăm chỉ" thuộc loại nào?', N'Tính từ chỉ phẩm chất.', 5),
('CH326', N'Chọn từ đúng để hoàn thành câu: "Chiếc áo này rất ___." (đẹp, chạy, uống, ngủ)', N'Hãy chọn tính từ phù hợp.', 5),
('CH327', N'Từ nào có nghĩa trái ngược với "buồn"? (vui, to, mới, thấp)', N'Hãy chọn từ trái nghĩa.', 5),
('CH328', N'Trong câu "Bà đang nấu cơm", từ "nấu" thuộc loại nào?', N'Động từ chỉ hành động.', 5),
('CH329', N'Chọn từ đúng hoàn thành câu: "Bạn nhỏ ___ tay chào cô giáo." (vẫy, xanh, ăn, đẹp)', N'Hãy chọn động từ phù hợp.', 5),
('CH330', N'Trong các từ sau, từ nào là danh từ? (quyển sách, vui, chạy, đỏ)', N'Danh từ là từ chỉ sự vật.', 5),



-- =========================================================
-- 5) TRÒ CHƠI: 40 câu (CHG001–CHG040)
-- Hoàn thiện câu từ (CHG001–CHG020)
('CHG001', N'Bé đang ___ bóng.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG002', N'Con mèo ___ trên sân.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG003', N'Bạn nhỏ rất ___ khi được khen.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG004', N'Mẹ mua cho em một chiếc ___ mới.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG005', N'Bố đang ___ xe máy.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG006', N'Em bé đang ___ sữa.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG007', N'Ông nội đang ___ báo.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG008', N'Cô giáo đang ___ bài.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG009', N'Bé Lan ___ tóc cho em gái.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG010', N'Trời hôm nay rất ___.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG011', N'Bạn Nam đang ___ nhà.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG012', N'Con chim đang ___ trên cành.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG013', N'Em đặt cặp lên ___.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG014', N'Bé đi học mang theo một cái ___.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG015', N'Bạn nhỏ đang ___ tranh.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG016', N'Em phải ___ tay trước khi ăn.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG017', N'Con chó chạy ___ sân.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG018', N'Bé cười rất ___.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG019', N'Bạn nhỏ đang ___ bánh.', N'Câu trò chơi: hoàn thiện câu.', 5),
('CHG020', N'Mẹ đang chuẩn bị ___ cho cả nhà.', N'Câu trò chơi: hoàn thiện câu.', 5),

-- Trùm tính nhẩm (CHG021–CHG040)
('CHG021', N'Tính: 6 + 3 × 4 = ?', N'Câu trò chơi tính nhẩm: hãy làm phép nhân trước.', 5),
('CHG022', N'Tính: (10 - 2) × 3 = ?', N'Tính trong ngoặc rồi nhân.', 5),
('CHG023', N'Tính: 25 ÷ 5 + 7 = ?', N'Chia trước, sau đó cộng.', 5),
('CHG024', N'Tính: 18 - 6 × 2 = ?', N'Nhân trước, rồi trừ.', 5),
('CHG025', N'Tính: (12 ÷ 3) + 15 = ?', N'Tính trong ngoặc, rồi cộng.', 5),
('CHG026', N'Tính: 9 × 5 - 8 = ?', N'Nhân trước, rồi trừ.', 5),
('CHG027', N'Tính: 50 ÷ (10 - 5) = ?', N'Tính trong ngoặc, rồi chia.', 5),
('CHG028', N'Tính: 7 × 4 + 6 = ?', N'Nhân trước, rồi cộng.', 5),
('CHG029', N'Tính: (20 - 8) ÷ 2 = ?', N'Tính trong ngoặc, rồi chia.', 5),
('CHG030', N'Tính: 36 ÷ 6 + 4 × 2 = ?', N'Chia và nhân trước, sau đó cộng.', 5),
('CHG031', N'Tính: 45 - 9 × 4 = ?', N'Nhân trước, rồi trừ.', 5),
('CHG032', N'Tính: (16 + 8) ÷ 4 = ?', N'Tính trong ngoặc, rồi chia.', 5),
('CHG033', N'Tính: 5 × 7 - 6 = ?', N'Nhân trước, rồi trừ.', 5),
('CHG034', N'Tính: 72 ÷ 8 + 9 = ?', N'Chia trước, rồi cộng.', 5),
('CHG035', N'Tính: (18 - 3) × 2 = ?', N'Tính trong ngoặc, rồi nhân.', 5),
('CHG036', N'Tính: 63 ÷ 9 + 5 = ?', N'Chia trước, rồi cộng.', 5),
('CHG037', N'Tính: 14 × 2 - 7 = ?', N'Nhân trước, rồi trừ.', 5),
('CHG038', N'Tính: (9 + 12) ÷ 3 = ?', N'Tính trong ngoặc, rồi chia.', 5),
('CHG039', N'Tính: 48 ÷ (12 - 8) = ?', N'Tính trong ngoặc, rồi chia.', 5),
('CHG040', N'Tính: 30 - 6 × 3 = ?', N'Nhân trước, rồi trừ.', 5);
GO
---------------------------------------------
-- TẠO DAPAN CHO CÁC CÂU TOÁN
---------------------------------------------
-- Bảng tạm lưu đáp án ĐÚNG cho các câu TOÁN
IF OBJECT_ID('tempdb..#MathKey') IS NOT NULL DROP TABLE #MathKey;
CREATE TABLE #MathKey (
    MaCauHoi  VARCHAR(10) PRIMARY KEY,
    DapAnDung INT NOT NULL
);

INSERT INTO #MathKey (MaCauHoi, DapAnDung) VALUES
    -- CỦNG CỐ TOÁN CH001–CH100
    ('CH001',  5),  -- 2 + 3
    ('CH002',  4),  -- 5 - 1
    ('CH003',  8),  -- 4 + 4
    ('CH004',  6),  -- 9 - 3
    ('CH005',  8),  -- 7 + 1
    ('CH006',  7),  -- 12 - 5
    ('CH007', 14),  -- 8 + 6
    ('CH008',  9),  -- 13 - 4
    ('CH009',  8),  -- 15 - 7
    ('CH010', 17),  -- 9 + 8

    ('CH011',  9),  -- 3 + 6
    ('CH012',  8),  -- 10 - 2
    ('CH013',  9),  -- 1 + 8
    ('CH014',  2),  -- 6 - 4
    ('CH015', 10),  -- 7 + 3
    ('CH016',  8),  -- 14 - 6
    ('CH017', 16),  -- 11 + 5
    ('CH018',  9),  -- 12 - 3
    ('CH019',  7),  -- 16 - 9
    ('CH020', 16),  -- 9 + 7

    ('CH021',  7),  -- 5 + 2
    ('CH022',  8),  -- 9 - 1
    ('CH023',  9),  -- 4 + 5
    ('CH024',  6),  -- 8 - 2
    ('CH025', 10),  -- 3 + 7
    ('CH026',  7),  -- 11 - 4
    ('CH027', 15),  -- 6 + 9
    ('CH028',  7),  -- 13 - 6
    ('CH029',  9),  -- 17 - 8
    ('CH030', 18),  -- 9 + 9

    ('CH031', 10),  -- 4 + 6
    ('CH032',  5),  -- 7 - 2
    ('CH033', 11),  -- 2 + 9
    ('CH034',  5),  -- 10 - 5
    ('CH035',  7),  -- 3 + 4
    ('CH036',  9),  -- 15 - 6
    ('CH037', 15),  -- 8 + 7
    ('CH038',  9),  -- 14 - 5
    ('CH039',  9),  -- 18 - 9
    ('CH040', 19),  -- 11 + 8

    ('CH041',  9),  -- 5 + 4
    ('CH042',  5),  -- 6 - 1
    ('CH043', 11),  -- 9 + 2
    ('CH044',  4),  -- 7 - 3
    ('CH045', 11),  -- 3 + 8
    ('CH046',  8),  -- 12 - 4
    ('CH047', 13),  -- 8 + 5
    ('CH048',  7),  -- 14 - 7
    ('CH049',  8),  -- 16 - 8
    ('CH050', 16),  -- 7 + 9

    ('CH051', 12),  -- 6 × 2
    ('CH052',  4),  -- 12 ÷ 3
    ('CH053', 19),  -- 7 + 12
    ('CH054', 11),  -- 20 - 9
    ('CH055', 15),  -- 5 × 3
    ('CH056',  9),  -- 18 ÷ 2
    ('CH057', 29),  -- 14 + 15
    ('CH058', 15),  -- 28 - 13
    ('CH059', 36),  -- 9 × 4
    ('CH060',  6),  -- 36 ÷ 6

    ('CH061', 12),  -- 4 × 3
    ('CH062',  4),  -- 16 ÷ 4
    ('CH063', 19),  -- 11 + 8
    ('CH064', 11),  -- 25 - 14
    ('CH065', 18),  -- 3 × 6
    ('CH066',  7),  -- 21 ÷ 3
    ('CH067', 29),  -- 17 + 12
    ('CH068', 15),  -- 32 - 17
    ('CH069', 40),  -- 8 × 5
    ('CH070',  6),  -- 42 ÷ 7

    ('CH071', 21),  -- 7 × 3
    ('CH072',  9),  -- 18 ÷ 2
    ('CH073', 23),  -- 14 + 9
    ('CH074', 13),  -- 29 - 16
    ('CH075', 18),  -- 9 × 2
    ('CH076',  6),  -- 30 ÷ 5
    ('CH077', 30),  -- 13 + 17
    ('CH078', 19),  -- 33 - 14
    ('CH079', 42),  -- 6 × 7
    ('CH080',  6),  -- 48 ÷ 8

    ('CH081', 24),  -- 8 × 3
    ('CH082',  5),  -- 20 ÷ 4
    ('CH083', 28),  -- 15 + 13
    ('CH084', 12),  -- 27 - 15
    ('CH085', 20),  -- 5 × 4
    ('CH086',  4),  -- 24 ÷ 6
    ('CH087', 29),  -- 18 + 11
    ('CH088', 16),  -- 35 - 19
    ('CH089', 45),  -- 9 × 5
    ('CH090',  5),  -- 45 ÷ 9

    ('CH091', 24),  -- 6 × 4
    ('CH092',  7),  -- 28 ÷ 4
    ('CH093', 31),  -- 19 + 12
    ('CH094', 13),  -- 31 - 18
    ('CH095', 21),  -- 7 × 3
    ('CH096',  4),  -- 32 ÷ 8
    ('CH097', 38),  -- 21 + 17
    ('CH098', 17),  -- 40 - 23
    ('CH099', 48),  -- 8 × 6
    ('CH100',  9),  -- 54 ÷ 6

    -- ÔN LUYỆN TOÁN
    -- OL001 (CH201–CH205)
    ('CH201', 11),  -- 7 + 4
    ('CH202',  9),  -- 15 - 6
    ('CH203',  9),  -- 3 × 3
    ('CH204',  4),  -- 16 ÷ 4
    ('CH205', 17),  -- 12 + 5

    -- OL002 (CH211–CH215)
    ('CH211',  9),  -- 18 - 9
    ('CH212', 16),  -- 9 + 7
    ('CH213', 20),  -- 4 × 5
    ('CH214',  4),  -- 20 ÷ 5
    ('CH215', 24),  -- 13 + 11

    -- OL003 (CH221–CH225)
    ('CH221', 17),  -- 25 - 8
    ('CH222', 20),  -- 14 + 6
    ('CH223', 18),  -- 6 × 3
    ('CH224',  5),  -- 30 ÷ 6
    ('CH225', 29),  -- 17 + 12

    -- OL004 (CH231–CH235)
    ('CH231', 15),  -- 28 - 13
    ('CH232', 34),  -- 19 + 15
    ('CH233', 28),  -- 7 × 4
    ('CH234',  8),  -- 40 ÷ 5
    ('CH235', 17),  -- 26 - 9

    -- OL005 (CH241–CH245)
    ('CH241', 19),  -- 36 - 17
    ('CH242', 34),  -- 18 + 16
    ('CH243', 48),  -- 8 × 6
    ('CH244',  5),  -- 45 ÷ 9
    ('CH245', 19),  -- 33 - 14

    -- OL006 (CH251–CH255)
    ('CH251', 24),  -- 42 - 18
    ('CH252', 46),  -- 27 + 19
    ('CH253', 63),  -- 9 × 7
    ('CH254',  5),  -- 50 ÷ 10
    ('CH255', 17),  -- 38 - 21

    -- OL007 (CH261–CH265)
    ('CH261', 29),  -- 56 - 27
    ('CH262', 59),  -- 31 + 28
    ('CH263', 48),  -- 12 × 4
    ('CH264',  8),  -- 64 ÷ 8
    ('CH265', 22),  -- 45 - 23

    -- OL008 (CH271–CH275)
    ('CH271', 34),  -- 63 - 29
    ('CH272', 61),  -- 34 + 27
    ('CH273', 66),  -- 11 × 6
    ('CH274',  8),  -- 72 ÷ 9
    ('CH275', 39),  -- 58 - 19

    -- OL009 (CH281–CH285)
    ('CH281', 38),  -- 72 - 34
    ('CH282', 69),  -- 43 + 26
    ('CH283', 91),  -- 13 × 7
    ('CH284',  9),  -- 81 ÷ 9
    ('CH285', 42),  -- 67 - 25


    -- GAME: TRÙM TÍNH NHẨM CHG021–CHG040
    ('CHG021', 18), -- 6 + 3×4
    ('CHG022', 24), -- (10-2)×3
    ('CHG023', 12), -- 25÷5 +7
    ('CHG024',  6), -- 18-6×2
    ('CHG025', 19), -- (12÷3)+15
    ('CHG026', 37), -- 9×5 -8
    ('CHG027', 10), -- 50÷(10-5)
    ('CHG028', 34), -- 7×4+6
    ('CHG029',  6), -- (20-8)÷2
    ('CHG030', 14), -- 36÷6+4×2
    ('CHG031',  9), -- 45-9×4
    ('CHG032',  6), -- (16+8)÷4
    ('CHG033', 29), -- 5×7-6
    ('CHG034', 18), -- 72÷8+9
    ('CHG035', 30), -- (18-3)×2
    ('CHG036', 12), -- 63÷9+5
    ('CHG037', 21), -- 14×2-7
    ('CHG038',  7), -- (9+12)÷3
    ('CHG039', 12), -- 48÷(12-8)
    ('CHG040', 12); -- 30-6×3
GO
--Tạo 3 đáp án sai
;WITH Q AS (
    SELECT 
        ROW_NUMBER() OVER (ORDER BY C.MaCauHoi) AS QNum,
        C.MaCauHoi,
        M.DapAnDung
    FROM CauHoi C
    JOIN #MathKey M ON C.MaCauHoi = M.MaCauHoi
),
Pos AS (
    SELECT 
        QNum,
        MaCauHoi,
        DapAnDung,
        ((QNum - 1) % 4) + 1 AS PosDung,           -- vị trí 1..4 sẽ là đáp án đúng
        DapAnDung + 1 AS Sai1,
        DapAnDung - 1 AS Sai2,
        DapAnDung + 2 AS Sai3
    FROM Q
),
A AS (
    SELECT 
        P.QNum,
        P.MaCauHoi,
        V.AnsIndex,
        P.PosDung,
        -- chọn nội dung theo vị trí đúng / sai
        CASE 
            WHEN V.AnsIndex = P.PosDung THEN P.DapAnDung
            WHEN V.AnsIndex = ((P.PosDung) % 4) + 1 THEN P.Sai1
            WHEN V.AnsIndex = ((P.PosDung + 1) % 4) + 1 THEN P.Sai2
            ELSE P.Sai3
        END AS GiaTri,
        ((P.QNum - 1) * 4 + V.AnsIndex) AS DapSo
    FROM Pos P
    CROSS APPLY (VALUES (1),(2),(3),(4)) V(AnsIndex)
)
INSERT INTO DapAn (MaDapAn, MaCauHoi, NoiDungDapAn, LaDapAnDung)
SELECT
    'DA' + RIGHT('0000' + CAST(DapSo AS VARCHAR(10)), 4) AS MaDapAn,
    MaCauHoi,
    CAST(GiaTri AS NVARCHAR(10)) AS NoiDungDapAn,
    CASE WHEN AnsIndex = PosDung THEN 1 ELSE 0 END AS LaDapAnDung
FROM A
ORDER BY MaCauHoi, AnsIndex;
GO

---------------------------------------------
-- TẠO DAPAN CHO CÁC CÂU TV
---------------------------------------------
USE UngDungHocTapChoTre;
GO

-- Thêm đáp án mới, rõ nghĩa, xoay vị trí đáp án đúng
INSERT INTO DapAn (MaDapAn, MaCauHoi, NoiDungDapAn, LaDapAnDung)
VALUES
	-- CH101: (bé, chạy, đẹp, nhanh) -> đúng: "bé"
    ('DA2001', 'CH101', N'chạy',   0),
    ('DA2002', 'CH101', N'nhanh',  0),
    ('DA2003', 'CH101', N'bé',     1),
    ('DA2004', 'CH101', N'đẹp',    0),

    -- CH102: (mèo, cao, ăn, đỏ) -> đúng: "mèo"
    ('DA2005', 'CH102', N'ăn',     0),
    ('DA2006', 'CH102', N'mèo',    1),
    ('DA2007', 'CH102', N'đỏ',     0),
    ('DA2008', 'CH102', N'cao',    0),

    -- CH103: (cái bàn, ngủ, to, nhanh) -> đúng: "cái bàn"
    ('DA2009', 'CH103', N'cái bàn', 1),
    ('DA2010', 'CH103', N'nhanh',   0),
    ('DA2011', 'CH103', N'to',      0),
    ('DA2012', 'CH103', N'ngủ',     0),

    -- CH104: (quả cam, viết, đẹp, mệt) -> đúng: "quả cam"
    ('DA2013', 'CH104', N'mệt',     0),
    ('DA2014', 'CH104', N'viết',    0),
    ('DA2015', 'CH104', N'quả cam', 1),
    ('DA2016', 'CH104', N'đẹp',     0),

    -- CH105: (bố, nhỏ, nhảy, vàng) -> đúng: "bố"
    ('DA2017', 'CH105', N'nhảy',    0),
    ('DA2018', 'CH105', N'bố',      1),
    ('DA2019', 'CH105', N'vàng',    0),
    ('DA2020', 'CH105', N'nhỏ',     0),

    -- CH106: (con chó, xanh, đi, cao) -> đúng: "con chó"
    ('DA2021', 'CH106', N'con chó', 1),
    ('DA2022', 'CH106', N'cao',     0),
    ('DA2023', 'CH106', N'xanh',    0),
    ('DA2024', 'CH106', N'đi',      0),

    -- CH107: (ngôi nhà, buồn, ăn, thấp) -> đúng: "ngôi nhà"
    ('DA2025', 'CH107', N'buồn',    0),
    ('DA2026', 'CH107', N'thấp',    0),
    ('DA2027', 'CH107', N'ăn',      0),
    ('DA2028', 'CH107', N'ngôi nhà', 1),

    -- CH108: (cái ghế, chạy, vui, to) -> đúng: "cái ghế"
    ('DA2029', 'CH108', N'cái ghế', 1),
    ('DA2030', 'CH108', N'chạy',    0),
    ('DA2031', 'CH108', N'to',      0),
    ('DA2032', 'CH108', N'vui',     0),

    -- CH109: (quyển sách, ngủ, đỏ, đẹp) -> đúng: "quyển sách"
    ('DA2033', 'CH109', N'đỏ',      0),
    ('DA2034', 'CH109', N'quyển sách', 1),
    ('DA2035', 'CH109', N'ngủ',     0),
    ('DA2036', 'CH109', N'đẹp',     0),

    -- CH110: (con gà, nhanh, vui, to) -> đúng: "con gà"
    ('DA2037', 'CH110', N'con gà',  1),
    ('DA2038', 'CH110', N'nhanh',   0),
    ('DA2039', 'CH110', N'to',      0),
    ('DA2040', 'CH110', N'vui',     0),

    -- CH111: (bạn Nam, hát, cao, buồn) -> đúng: "bạn Nam"
    ('DA2041', 'CH111', N'hát',     0),
    ('DA2042', 'CH111', N'cao',     0),
    ('DA2043', 'CH111', N'buồn',    0),
    ('DA2044', 'CH111', N'bạn Nam', 1),

    -- CH112: (cái bóng, vui, ăn, sạch) -> đúng: "cái bóng"
    ('DA2045', 'CH112', N'ăn',      0),
    ('DA2046', 'CH112', N'cái bóng', 1),
    ('DA2047', 'CH112', N'sạch',    0),
    ('DA2048', 'CH112', N'vui',     0),

    -- CH113: (mẹ, chạy, xanh, nhỏ) -> đúng: "mẹ"
    ('DA2049', 'CH113', N'chạy',    0),
    ('DA2050', 'CH113', N'nhỏ',     0),
    ('DA2051', 'CH113', N'mẹ',      1),
    ('DA2052', 'CH113', N'xanh',    0),

    -- CH114: (cha, vui, to, đi) -> đúng: "cha"
    ('DA2053', 'CH114', N'to',      0),
    ('DA2054', 'CH114', N'đi',      0),
    ('DA2055', 'CH114', N'cha',     1),
    ('DA2056', 'CH114', N'vui',     0),

    -- CH115: (em bé, xinh, ăn, ngồi) -> đúng: "em bé"
    ('DA2057', 'CH115', N'em bé',   1),
    ('DA2058', 'CH115', N'ngồi',    0),
    ('DA2059', 'CH115', N'ăn',      0),
    ('DA2060', 'CH115', N'xinh',    0),

    -- CH116: (quả táo, đỏ, mở, nhanh) -> đúng: "quả táo"
    ('DA2061', 'CH116', N'mở',      0),
    ('DA2062', 'CH116', N'quả táo', 1),
    ('DA2063', 'CH116', N'đỏ',      0),
    ('DA2064', 'CH116', N'nhanh',   0),

    -- CH117: (bạn Lan, cao, chạy, mệt) -> đúng: "bạn Lan"
    ('DA2065', 'CH117', N'cao',     0),
    ('DA2066', 'CH117', N'mệt',     0),
    ('DA2067', 'CH117', N'chạy',    0),
    ('DA2068', 'CH117', N'bạn Lan', 1),

    -- CH118: (bạn nhỏ, buồn, khóc, xinh) -> đúng: "bạn nhỏ"
    ('DA2069', 'CH118', N'bạn nhỏ', 1),
    ('DA2070', 'CH118', N'buồn',    0),
    ('DA2071', 'CH118', N'xinh',    0),
    ('DA2072', 'CH118', N'khóc',    0),

    -- CH119: (cái nón, vàng, ăn, lạnh) -> đúng: "cái nón"
    ('DA2073', 'CH119', N'ăn',      0),
    ('DA2074', 'CH119', N'lạnh',    0),
    ('DA2075', 'CH119', N'cái nón', 1),
    ('DA2076', 'CH119', N'vàng',    0),

    -- CH120: (mặt trời, sáng, vui, thấp) -> đúng: "mặt trời"
    ('DA2077', 'CH120', N'vui',     0),
    ('DA2078', 'CH120', N'mặt trời', 1),
    ('DA2079', 'CH120', N'sáng',    0),
    ('DA2080', 'CH120', N'thấp',    0),
    -- CH121: đúng "ăn"
    ('DA2081', 'CH121', N'đỏ',   0),
    ('DA2082', 'CH121', N'ăn',   1),
    ('DA2083', 'CH121', N'bé',   0),
    ('DA2084', 'CH121', N'đẹp',  0),

    -- CH122: đúng "nhảy"
    ('DA2085', 'CH122', N'mèo',   0),
    ('DA2086', 'CH122', N'nhảy',  1),
    ('DA2087', 'CH122', N'cao',   0),
    ('DA2088', 'CH122', N'vàng',  0),

    -- CH123: đúng "chạy"
    ('DA2089', 'CH123', N'chạy',  1),
    ('DA2090', 'CH123', N'vui',   0),
    ('DA2091', 'CH123', N'xanh',  0),
    ('DA2092', 'CH123', N'nhỏ',   0),

    -- CH124: đúng "ngủ"
    ('DA2093', 'CH124', N'to',    0),
    ('DA2094', 'CH124', N'bàn',   0),
    ('DA2095', 'CH124', N'ngủ',   1),
    ('DA2096', 'CH124', N'mệt',   0),

    -- CH125: đúng "hát"
    ('DA2097', 'CH125', N'thấp',  0),
    ('DA2098', 'CH125', N'sách',  0),
    ('DA2099', 'CH125', N'hát',   1),
    ('DA2100', 'CH125', N'vàng',  0),

    -- CH126: đúng "đọc"
    ('DA2101', 'CH126', N'ghế',   0),
    ('DA2102', 'CH126', N'buồn',  0),
    ('DA2103', 'CH126', N'đọc',   1),
    ('DA2104', 'CH126', N'ấm',    0),

    -- CH127: đúng "đi"
    ('DA2105', 'CH127', N'bé',    0),
    ('DA2106', 'CH127', N'đi',    1),
    ('DA2107', 'CH127', N'cây',   0),
    ('DA2108', 'CH127', N'cao',   0),

    -- CH128: đúng "cười"
    ('DA2109', 'CH128', N'cười',  1),
    ('DA2110', 'CH128', N'mèo',   0),
    ('DA2111', 'CH128', N'đẹp',   0),
    ('DA2112', 'CH128', N'to',    0),

    -- CH129: đúng "nhìn"
    ('DA2113', 'CH129', N'xanh',  0),
    ('DA2114', 'CH129', N'vui',   0),
    ('DA2115', 'CH129', N'nhìn',  1),
    ('DA2116', 'CH129', N'táo',   0),

    -- CH130: đúng "bay"
    ('DA2117', 'CH130', N'nhỏ',   0),
    ('DA2118', 'CH130', N'bút',   0),
    ('DA2119', 'CH130', N'bay',   1),
    ('DA2120', 'CH130', N'đỏ',    0),

    -- CH131: đúng "viết"
    ('DA2121', 'CH131', N'cao',   0),
    ('DA2122', 'CH131', N'viết',  1),
    ('DA2123', 'CH131', N'nước',  0),
    ('DA2124', 'CH131', N'lạnh',  0),

    -- CH132: đúng "ngồi"
    ('DA2125', 'CH132', N'ngồi',      1),
    ('DA2126', 'CH132', N'bông hoa',  0),
    ('DA2127', 'CH132', N'sạch',      0),
    ('DA2128', 'CH132', N'to',        0),

    -- CH133: đúng "bơi"
    ('DA2129', 'CH133', N'mệt',      0),
    ('DA2130', 'CH133', N'bơi',      1),
    ('DA2131', 'CH133', N'con chó',  0),
    ('DA2132', 'CH133', N'vàng',     0),

    -- CH134: đúng "mở"
    ('DA2133', 'CH134', N'xanh',     0),
    ('DA2134', 'CH134', N'cái ghế',  0),
    ('DA2135', 'CH134', N'mở',       1),
    ('DA2136', 'CH134', N'buồn',     0),

    -- CH135: đúng "đẩy"
    ('DA2137', 'CH135', N'to',       0),
    ('DA2138', 'CH135', N'đẩy',      1),
    ('DA2139', 'CH135', N'đồ chơi',  0),
    ('DA2140', 'CH135', N'vui',      0),

    -- CH136: đúng "nấu"
    ('DA2141', 'CH136', N'nấu',   1),
    ('DA2142', 'CH136', N'sách',  0),
    ('DA2143', 'CH136', N'cao',   0),
    ('DA2144', 'CH136', N'nóng',  0),

    -- CH137: đúng "uống"
    ('DA2145', 'CH137', N'vàng',     0),
    ('DA2146', 'CH137', N'bút chì',  0),
    ('DA2147', 'CH137', N'uống',     1),
    ('DA2148', 'CH137', N'mặn',      0),

    -- CH138: đúng "học"
    ('DA2149', 'CH138', N'ấm',    0),
    ('DA2150', 'CH138', N'đàn',   0),
    ('DA2151', 'CH138', N'xanh',  0),
    ('DA2152', 'CH138', N'học',   1),

    -- CH139: đúng "vẽ"
    ('DA2153', 'CH139', N'mỏng',    0),
    ('DA2154', 'CH139', N'vẽ',      1),
    ('DA2155', 'CH139', N'quả cam', 0),
    ('DA2156', 'CH139', N'cao',     0),

    -- CH140: đúng "leo"
    ('DA2157', 'CH140', N'buồn',    0),
    ('DA2158', 'CH140', N'leo',     1),
    ('DA2159', 'CH140', N'sạch',    0),
    ('DA2160', 'CH140', N'con mèo', 0),

    -- CH141: đúng "cao"
    ('DA2161', 'CH141', N'mèo',   0),
    ('DA2162', 'CH141', N'cao',   1),
    ('DA2163', 'CH141', N'chạy',  0),
    ('DA2164', 'CH141', N'uống',  0),

    -- CH142: đúng "đẹp"
    ('DA2165', 'CH142', N'cái bàn', 0),
    ('DA2166', 'CH142', N'đi',      0),
    ('DA2167', 'CH142', N'đẹp',     1),
    ('DA2168', 'CH142', N'ăn',      0),

    -- CH143: đúng "vui"
    ('DA2169', 'CH143', N'sách',  0),
    ('DA2170', 'CH143', N'vui',   1),
    ('DA2171', 'CH143', N'uống',  0),
    ('DA2172', 'CH143', N'chạy',  0),

    -- CH144: đúng "buồn"
    ('DA2173', 'CH144', N'con chó', 0),
    ('DA2174', 'CH144', N'ngồi',    0),
    ('DA2175', 'CH144', N'buồn',    1),
    ('DA2176', 'CH144', N'xanh',    0),

    -- CH145: đúng "xanh"
    ('DA2177', 'CH145', N'ghế',   0),
    ('DA2178', 'CH145', N'xanh',  1),
    ('DA2179', 'CH145', N'ăn',    0),
    ('DA2180', 'CH145', N'đá',    0),

    -- CH146: đúng "ấm"
    ('DA2181', 'CH146', N'mèo',   0),
    ('DA2182', 'CH146', N'chạy',  0),
    ('DA2183', 'CH146', N'ấm',    1),
    ('DA2184', 'CH146', N'to',    0),

    -- CH147: đúng "lạnh"
    ('DA2185', 'CH147', N'em bé', 0),
    ('DA2186', 'CH147', N'viết',  0),
    ('DA2187', 'CH147', N'nấu',   0),
    ('DA2188', 'CH147', N'lạnh',  1),

    -- CH148: đúng "mệt"
    ('DA2189', 'CH148', N'cái thước', 0),
    ('DA2190', 'CH148', N'mệt',       1),
    ('DA2191', 'CH148', N'đọc',       0),
    ('DA2192', 'CH148', N'cười',      0),

    -- CH149: đúng "nhanh"
    ('DA2193', 'CH149', N'bé',    0),
    ('DA2194', 'CH149', N'ngồi',  0),
    ('DA2195', 'CH149', N'nhanh', 1),
    ('DA2196', 'CH149', N'hát',   0),

    -- CH150: đúng "sạch"
    ('DA2197', 'CH150', N'bàn',   0),
    ('DA2198', 'CH150', N'ăn',    0),
    ('DA2199', 'CH150', N'sạch',  1),
    ('DA2200', 'CH150', N'uống',  0),

    -- CH151: đúng "vàng"
    ('DA2201', 'CH151', N'mèo',   0),
    ('DA2202', 'CH151', N'vàng',  1),
    ('DA2203', 'CH151', N'mở',    0),
    ('DA2204', 'CH151', N'nấu',   0),

    -- CH152: đúng "thấp"
    ('DA2205', 'CH152', N'sách',  0),
    ('DA2206', 'CH152', N'đi',    0),
    ('DA2207', 'CH152', N'thấp',  1),
    ('DA2208', 'CH152', N'hát',   0),

    -- CH153: đúng "to"
    ('DA2209', 'CH153', N'bút',   0),
    ('DA2210', 'CH153', N'to',    1),
    ('DA2211', 'CH153', N'ăn',    0),
    ('DA2212', 'CH153', N'ngồi',  0),

    -- CH154: đúng "nhỏ"
    ('DA2213', 'CH154', N'bé',    0),
    ('DA2214', 'CH154', N'chạy',  0),
    ('DA2215', 'CH154', N'nhỏ',   1),
    ('DA2216', 'CH154', N'mở',    0),

    -- CH155: đúng "hiền"
    ('DA2217', 'CH155', N'con chó', 0),
    ('DA2218', 'CH155', N'uống',    0),
    ('DA2219', 'CH155', N'bay',     0),
    ('DA2220', 'CH155', N'hiền',    1),

    -- CH156: đúng "ngọt"
    ('DA2221', 'CH156', N'quả táo', 0),
    ('DA2222', 'CH156', N'ngọt',    1),
    ('DA2223', 'CH156', N'nhảy',    0),
    ('DA2224', 'CH156', N'ngồi',    0),

    -- CH157: đúng "thơm"
    ('DA2225', 'CH157', N'hoa',   0),
    ('DA2226', 'CH157', N'nhìn',  0),
    ('DA2227', 'CH157', N'thơm',  1),
    ('DA2228', 'CH157', N'hát',   0),

    -- CH158: đúng "trẻ"
    ('DA2229', 'CH158', N'học sinh', 0),
    ('DA2230', 'CH158', N'viết',     0),
    ('DA2231', 'CH158', N'trẻ',      1),
    ('DA2232', 'CH158', N'đi',       0),

    -- CH159: đúng "dài"
    ('DA2233', 'CH159', N'cái thước', 0),
    ('DA2234', 'CH159', N'dài',       1),
    ('DA2235', 'CH159', N'đọc',       0),
    ('DA2236', 'CH159', N'khóc',      0),

    -- CH160: đúng "cao lớn"
    ('DA2237', 'CH160', N'bạn Nam', 0),
    ('DA2238', 'CH160', N'ngủ',     0),
    ('DA2239', 'CH160', N'cao lớn', 1),
    ('DA2240', 'CH160', N'nhảy',    0),

    -- CH161: đúng "nghỉ"
    ('DA2241', 'CH161', N'nghi~', 0),
    ('DA2242', 'CH161', N'nghỉ',  1),
    ('DA2243', 'CH161', N'nghỉ~', 0),
    ('DA2244', 'CH161', N'nghị~', 0),

    -- CH162: đúng "ngọt"
    ('DA2245', 'CH162', N'ngo~t', 0),
    ('DA2246', 'CH162', N'ngot~', 0),
    ('DA2247', 'CH162', N'ngọt',  1),
    ('DA2248', 'CH162', N'ngọt~', 0),

    -- CH163: đúng "trượt"
    ('DA2249', 'CH163', N'chượt',  0),
    ('DA2250', 'CH163', N'trượt',  1),
    ('DA2251', 'CH163', N'truột',  0),
    ('DA2252', 'CH163', N'chuột~', 0),

    -- CH164: đúng "thuyền"
    ('DA2253', 'CH164', N'thuỳen',  0),
    ('DA2254', 'CH164', N'thuyền',  1),
    ('DA2255', 'CH164', N'thuyên~', 0),
    ('DA2256', 'CH164', N'thuyển~', 0),

    -- CH165: đúng "giờ"
    ('DA2257', 'CH165', N'gờ',   0),
    ('DA2258', 'CH165', N'giơ~', 0),
    ('DA2259', 'CH165', N'giò',  0),
    ('DA2260', 'CH165', N'giờ',  1),

    -- CH166: đúng "bún"
    ('DA2261', 'CH166', N'búnn', 0),
    ('DA2262', 'CH166', N'bún',  1),
    ('DA2263', 'CH166', N'bủn',  0),
    ('DA2264', 'CH166', N'bùn~', 0),

    -- CH167: đúng "mảnh"
    ('DA2265', 'CH167', N'mãnh~', 0),
    ('DA2266', 'CH167', N'mánh',  0),
    ('DA2267', 'CH167', N'mảnh',  1),
    ('DA2268', 'CH167', N'manh~', 0),

    -- CH168: đúng "gạo"
    ('DA2269', 'CH168', N'gạoo', 0),
    ('DA2270', 'CH168', N'gảo',  0),
    ('DA2271', 'CH168', N'gạo',  1),
    ('DA2272', 'CH168', N'gao~', 0),

    -- CH169: đúng "lạnh"
    ('DA2273', 'CH169', N'lanh~', 0),
    ('DA2274', 'CH169', N'lạnh',  1),
    ('DA2275', 'CH169', N'lãnh~', 0),
    ('DA2276', 'CH169', N'lảnh~', 0),

    -- CH170: đúng "nghiêng"
    ('DA2277', 'CH170', N'ngiêng',  0),
    ('DA2278', 'CH170', N'nghieng', 0),
    ('DA2279', 'CH170', N'nghiêng', 1),
    ('DA2280', 'CH170', N'nghiền~', 0),

    -- CH171: đúng "mèo"
    ('DA2281', 'CH171', N'meò',  0),
    ('DA2282', 'CH171', N'mèo~', 0),
    ('DA2283', 'CH171', N'mèo',  1),
    ('DA2284', 'CH171', N'meo~', 0),

    -- CH172: đúng "mũi"
    ('DA2285', 'CH172', N'mũii', 0),
    ('DA2286', 'CH172', N'mùi~', 0),
    ('DA2287', 'CH172', N'mui~', 0),
    ('DA2288', 'CH172', N'mũi',  1),

    -- CH173: đúng "đẹp"
    ('DA2289', 'CH173', N'đẹpp', 0),
    ('DA2290', 'CH173', N'đẹp',  1),
    ('DA2291', 'CH173', N'dẹp',  0),
    ('DA2292', 'CH173', N'đẹp~', 0),

    -- CH174: đúng "vẽ"
    ('DA2293', 'CH174', N've~,', 0),
    ('DA2294', 'CH174', N've~',  0),
    ('DA2295', 'CH174', N'vẽ',   1),
    ('DA2296', 'CH174', N'vẽ~',  0),

    -- CH175: đúng "gió"
    ('DA2297', 'CH175', N'giáo~', 0),
    ('DA2298', 'CH175', N'gió~',  0),
    ('DA2299', 'CH175', N'gió',   1),
    ('DA2300', 'CH175', N'gio~',  0),

    -- CH176: đúng "cánh"
    ('DA2301', 'CH176', N'cáng~', 0),
    ('DA2302', 'CH176', N'cãnh~', 0),
    ('DA2303', 'CH176', N'cánh',  1),
    ('DA2304', 'CH176', N'cánh~', 0),

    -- CH177: đúng "bút"
    ('DA2305', 'CH177', N'bút',  1),
    ('DA2306', 'CH177', N'bụt',  0),
    ('DA2307', 'CH177', N'but~', 0),
    ('DA2308', 'CH177', N'búc',  0),

    -- CH178: đúng "mưa"
    ('DA2309', 'CH178', N'mư~',  0),
    ('DA2310', 'CH178', N'mưa~', 0),
    ('DA2311', 'CH178', N'mưa',  1),
    ('DA2312', 'CH178', N'mu~a', 0),

    -- CH179: đúng "sữa"
    ('DA2313', 'CH179', N'sưã',  0),
    ('DA2314', 'CH179', N'sữa~', 0),
    ('DA2315', 'CH179', N'su~a', 0),
    ('DA2316', 'CH179', N'sữa',  1),

    -- CH180: đúng "bàn"
    ('DA2317', 'CH180', N'bằn',  0),
    ('DA2318', 'CH180', N'ban~', 0),
    ('DA2319', 'CH180', N'bàn',  1),
    ('DA2320', 'CH180', N'bàn~', 0),

    -- CH181: Bé đang ___ bóng. (đúng: đá)
    ('DA2321', 'CH181', N'ăn',   0),
    ('DA2322', 'CH181', N'vẽ',   0),
    ('DA2323', 'CH181', N'đọc',  0),
    ('DA2324', 'CH181', N'đá',   1),

    -- CH182: Con mèo ___ trên ghế. (đúng: nằm)
    ('DA2325', 'CH182', N'nhảy', 0),
    ('DA2326', 'CH182', N'đi',   0),
    ('DA2327', 'CH182', N'nằm',  1),
    ('DA2328', 'CH182', N'vui',  0),

    -- CH183: Bạn Nam rất ___ khi được khen. (đúng: vui)
    ('DA2329', 'CH183', N'buồn', 0),
    ('DA2330', 'CH183', N'đẹp',  0),
    ('DA2331', 'CH183', N'cao',  0),
    ('DA2332', 'CH183', N'vui',  1),

    -- CH184: Mẹ mua cho em một ___ mới. (đúng: áo)
    ('DA2333', 'CH184', N'bút',  0),
    ('DA2334', 'CH184', N'sách', 0),
    ('DA2335', 'CH184', N'áo',   1),
    ('DA2336', 'CH184', N'quạt', 0),

    -- CH185: Em bé đang ___ sữa. (đúng: uống)
    ('DA2337', 'CH185', N'uống', 1),
    ('DA2338', 'CH185', N'nhảy', 0),
    ('DA2339', 'CH185', N'đọc',  0),
    ('DA2340', 'CH185', N'chơi', 0),

    -- CH186: Bố đi làm bằng ___. (đúng: xe máy)
    ('DA2341', 'CH186', N'xe đạp',  0),
    ('DA2342', 'CH186', N'ca nô',   0),
    ('DA2343', 'CH186', N'xe máy',  1),
    ('DA2344', 'CH186', N'máy bay', 0),

    -- CH187: Hôm nay trời rất ___. (đúng: đẹp)
    ('DA2345', 'CH187', N'buồn', 0),
    ('DA2346', 'CH187', N'đẹp',  1),
    ('DA2347', 'CH187', N'lạnh', 0),
    ('DA2348', 'CH187', N'tối',  0),

    -- CH188: Cô giáo đang ___ bài. (đúng: giảng)
    ('DA2349', 'CH188', N'vẽ',    0),
    ('DA2350', 'CH188', N'nhảy',  0),
    ('DA2351', 'CH188', N'giảng', 1),
    ('DA2352', 'CH188', N'chơi',  0),

    -- CH189: Bạn nhỏ đang ___ tranh. (đúng: tô)
    ('DA2353', 'CH189', N'xé',   0),
    ('DA2354', 'CH189', N'đẩy',  0),
    ('DA2355', 'CH189', N'tô',   1),
    ('DA2356', 'CH189', N'viết', 0),

    -- CH190: Con chó chạy ___ sân. (đúng: quanh)
    ('DA2357', 'CH190', N'vào',   0),
    ('DA2358', 'CH190', N'quanh', 1),
    ('DA2359', 'CH190', N'lên',   0),
    ('DA2360', 'CH190', N'xún',   0),

    -- CH191: Mẹ bảo em phải ___ tay trước khi ăn. (đúng: rửa)
    ('DA2361', 'CH191', N'nắm',  0),
    ('DA2362', 'CH191', N'rửa',  1),
    ('DA2363', 'CH191', N'dùng', 0),
    ('DA2364', 'CH191', N'mở',   0),

    -- CH192: Bé Lan đang ___ sách. (đúng: đọc)
    ('DA2365', 'CH192', N'quăng', 0),
    ('DA2366', 'CH192', N'mua',   0),
    ('DA2367', 'CH192', N'viết',  0),
    ('DA2368', 'CH192', N'đọc',   1),

    -- CH193: Ông nội đang ___ báo. (đúng: đọc)
    ('DA2369', 'CH193', N'đọc', 1),
    ('DA2370', 'CH193', N'đập', 0),
    ('DA2371', 'CH193', N'viết',0),
    ('DA2372', 'CH193', N'vẽ',  0),

    -- CH194: Chị gái em rất ___. (đúng: hiền)
    ('DA2373', 'CH194', N'cao',  0),
    ('DA2374', 'CH194', N'hiền', 1),
    ('DA2375', 'CH194', N'đỏ',   0),
    ('DA2376', 'CH194', N'lớn',  0),

    -- CH195: Em đi học mang theo một chiếc ___. (đúng: cặp)
    ('DA2377', 'CH195', N'kéo',  0),
    ('DA2378', 'CH195', N'bút',  0),
    ('DA2379', 'CH195', N'sô',   0),
    ('DA2380', 'CH195', N'cặp',  1),

    -- CH196: Bạn nhỏ chơi ___ cùng bạn. (đúng: bóng)
    ('DA2381', 'CH196', N'cờ',   0),
    ('DA2382', 'CH196', N'bóng', 1),
    ('DA2383', 'CH196', N'lá',   0),
    ('DA2384', 'CH196', N'kéo',  0),

    -- CH197: Bé cười rất ___. (đúng: tươi)
    ('DA2385', 'CH197', N'lạnh', 0),
    ('DA2386', 'CH197', N'chậm', 0),
    ('DA2387', 'CH197', N'tươi', 1),
    ('DA2388', 'CH197', N'tối',  0),

    -- CH198: Em đặt cặp lên ___. (đúng: bàn)
    ('DA2389', 'CH198', N'ghế',   0),
    ('DA2390', 'CH198', N'sàn',   0),
    ('DA2391', 'CH198', N'tường', 0),
    ('DA2392', 'CH198', N'bàn',   1),

    -- CH199: Bạn nhỏ đang ___ bánh. (đúng: ăn)
    ('DA2393', 'CH199', N'nướng', 0),
    ('DA2394', 'CH199', N'ăn',    1),
    ('DA2395', 'CH199', N'trét',  0),
    ('DA2396', 'CH199', N'xé',    0),

    -- CH200: Mẹ đang chuẩn bị ___ cho cả nhà. (đúng: bữa ăn)
    ('DA2397', 'CH200', N'bữa ăn',  1),
    ('DA2398', 'CH200', N'quần áo', 0),
    ('DA2399', 'CH200', N'bánh',    0),
    ('DA2400', 'CH200', N'nước',    0),

--------------------------------------------------
    -- PHẦN ÔN LUYỆN
-------------------------------------------------
	
-- MỨC DỄ
    -- CH206: Danh từ? (bàn, chạy, đỏ, cao) -> đúng: "bàn"
    ('DA2401', 'CH206', N'đỏ',   0),
    ('DA2402', 'CH206', N'chạy', 0),
    ('DA2403', 'CH206', N'bàn',  1),
    ('DA2404', 'CH206', N'cao',  0),

    -- CH207: Động từ? (ăn, mèo, đẹp, bé) -> đúng: "ăn"
    ('DA2405', 'CH207', N'ăn',   1),
    ('DA2406', 'CH207', N'mèo',  0),
    ('DA2407', 'CH207', N'bé',   0),
    ('DA2408', 'CH207', N'đẹp',  0),

    -- CH208: Hoàn thiện: "Bé đang ___ sách." -> đúng: "đọc"
    ('DA2409', 'CH208', N'vẽ',   0),
    ('DA2410', 'CH208', N'đọc',  1),
    ('DA2411', 'CH208', N'chơi', 0),
    ('DA2412', 'CH208', N'ngủ',  0),

    -- CH209: Từ nào viết đúng? (ngọt, ngoạt~, ngọt~, ngọt~) -> "ngọt"
    ('DA2413', 'CH209', N'ngoạt~', 0),
    ('DA2414', 'CH209', N'ngọt',   1),
    ('DA2415', 'CH209', N'ngọt~',  0),
    ('DA2416', 'CH209', N'ngọtt~', 0),

    -- CH210: Tính từ? (xanh, chạy, ăn, ghế) -> "xanh"
    ('DA2417', 'CH210', N'chạy', 0),
    ('DA2418', 'CH210', N'xanh', 1),
    ('DA2419', 'CH210', N'ăn',   0),
    ('DA2420', 'CH210', N'ghế',  0),

    -- CH216: Từ nào viết đúng? (mưa, mư~, mu~a, mưa~) -> "mưa"
    ('DA2421', 'CH216', N'mư~',  0),
    ('DA2422', 'CH216', N'mu~a', 0),
    ('DA2423', 'CH216', N'mưa',  1),
    ('DA2424', 'CH216', N'mưa~', 0),

    -- CH217: Chọn động từ: (nhảy, đẹp, bàn, vàng) -> "nhảy"
    ('DA2425', 'CH217', N'nhảy', 1),
    ('DA2426', 'CH217', N'bàn',  0),
    ('DA2427', 'CH217', N'đẹp',  0),
    ('DA2428', 'CH217', N'vàng', 0),

    -- CH218: "Con chó đang ___ xương." -> "gặm"
    ('DA2429', 'CH218', N'đọc',  0),
    ('DA2430', 'CH218', N'vẽ',   0),
    ('DA2431', 'CH218', N'gặm',  1),
    ('DA2432', 'CH218', N'nhìn', 0),

    -- CH219: Danh từ? (cái ghế, đỏ, chạy, mệt) -> "cái ghế"
    ('DA2433', 'CH219', N'đỏ',      0),
    ('DA2434', 'CH219', N'chạy',    0),
    ('DA2435', 'CH219', N'cái ghế', 1),
    ('DA2436', 'CH219', N'mệt',     0),

    -- CH220: Tính từ? (buồn, chạy, ăn, mèo) -> "buồn"
    ('DA2437', 'CH220', N'chạy', 0),
    ('DA2438', 'CH220', N'ăn',   0),
    ('DA2439', 'CH220', N'mèo',  0),
    ('DA2440', 'CH220', N'buồn', 1),

    -- CH226: Động từ? (viết, đỏ, bé, vàng) -> "viết"
    ('DA2441', 'CH226', N'viết', 1),
    ('DA2442', 'CH226', N'bé',   0),
    ('DA2443', 'CH226', N'đỏ',   0),
    ('DA2444', 'CH226', N'vàng', 0),

    -- CH227: Danh từ? (con mèo, xanh, ăn, vui) -> "con mèo"
    ('DA2445', 'CH227', N'xanh',    0),
    ('DA2446', 'CH227', N'con mèo', 1),
    ('DA2447', 'CH227', N'vui',     0),
    ('DA2448', 'CH227', N'ăn',      0),

    -- CH228: "Mẹ đang ___ cơm." -> "nấu"
    ('DA2449', 'CH228', N'vẽ',   0),
    ('DA2450', 'CH228', N'giặt', 0),
    ('DA2451', 'CH228', N'nấu',  1),
    ('DA2452', 'CH228', N'chơi', 0),

    -- CH229: Từ viết đúng? (bút, but~, bụt, búc) -> "bút"
    ('DA2453', 'CH229', N'but~', 0),
    ('DA2454', 'CH229', N'bút',  1),
    ('DA2455', 'CH229', N'búc',  0),
    ('DA2456', 'CH229', N'bụt',  0),

    -- CH230: Tính từ? (dài, chạy, ăn, ngủ) -> "dài"
    ('DA2457', 'CH230', N'chạy', 0),
    ('DA2458', 'CH230', N'dài',  1),
    ('DA2459', 'CH230', N'ăn',   0),
    ('DA2460', 'CH230', N'ngủ',  0),

    -- CH236: "Bạn nhỏ rất ___ khi được khen." -> "vui"
    ('DA2461', 'CH236', N'buồn',    0),
    ('DA2462', 'CH236', N'vui',     1),
    ('DA2463', 'CH236', N'im lặng', 0),
    ('DA2464', 'CH236', N'lạnh',    0),

-- MỨC TRUNG BÌNH

    -- CH237: Động từ? (học, đẹp, mèo, xanh) -> "học"
    ('DA2465', 'CH237', N'đẹp',  0),
    ('DA2466', 'CH237', N'học',  1),
    ('DA2467', 'CH237', N'mèo',  0),
    ('DA2468', 'CH237', N'xanh', 0),

    -- CH238: Từ viết đúng? (vẽ, vẻ~, vẽ~, ve~) -> "vẽ"
    ('DA2469', 'CH238', N've~',  0),
    ('DA2470', 'CH238', N'vẽ~',  0),
    ('DA2471', 'CH238', N'vẽ',   1),
    ('DA2472', 'CH238', N'vẻ~',  0),

    -- CH239: Danh từ? (cái nón, chạy, to, nhanh) -> "cái nón"
    ('DA2473', 'CH239', N'cái nón', 1),
    ('DA2474', 'CH239', N'to',      0),
    ('DA2475', 'CH239', N'chạy',    0),
    ('DA2476', 'CH239', N'nhanh',   0),

    -- CH240: Tính từ? (ấm, chạy, viết, mèo) -> "ấm"
    ('DA2477', 'CH240', N'chạy', 0),
    ('DA2478', 'CH240', N'viết', 0),
    ('DA2479', 'CH240', N'ấm',   1),
    ('DA2480', 'CH240', N'mèo',  0),

    -- CH246: "Quyển vở có màu ___." -> "xanh"
    ('DA2481', 'CH246', N'ăn',   0),
    ('DA2482', 'CH246', N'cao',  0),
    ('DA2483', 'CH246', N'xanh', 1),
    ('DA2484', 'CH246', N'ngọt', 0),

    -- CH247: Động từ? (ngồi, bàn, xanh, vàng) -> "ngồi"
    ('DA2485', 'CH247', N'bàn',  0),
    ('DA2486', 'CH247', N'xanh', 0),
    ('DA2487', 'CH247', N'ngồi', 1),
    ('DA2488', 'CH247', N'vàng', 0),

    -- CH248: Tính từ? (nhỏ, mèo, ăn, uống) -> "nhỏ"
    ('DA2489', 'CH248', N'mèo',  0),
    ('DA2490', 'CH248', N'nhỏ',  1),
    ('DA2491', 'CH248', N'ăn',   0),
    ('DA2492', 'CH248', N'uống', 0),

    -- CH249: Danh từ? (quả táo, đỏ, chạy, mệt) -> "quả táo"
    ('DA2493', 'CH249', N'đỏ',      0),
    ('DA2494', 'CH249', N'quả táo', 1),
    ('DA2495', 'CH249', N'mệt',     0),
    ('DA2496', 'CH249', N'chạy',    0),

    -- CH250: Từ viết đúng? (mũi, mu~i, mũii, mủi) -> "mũi"
    ('DA2497', 'CH250', N'mũii', 0),
    ('DA2498', 'CH250', N'mu~i', 0),
    ('DA2499', 'CH250', N'mũi',  1),
    ('DA2500', 'CH250', N'mủi',  0),

    -- CH256: "Bé Lan đang ___ bài." -> "làm"
    ('DA2501', 'CH256', N'xóa',  0),
    ('DA2502', 'CH256', N'tô',   0),
    ('DA2503', 'CH256', N'làm',  1),
    ('DA2504', 'CH256', N'uống', 0),

    -- CH257: Danh từ? (cái nhà, đẹp, ăn, vui) -> "cái nhà"
    ('DA2505', 'CH257', N'đẹp',     0),
    ('DA2506', 'CH257', N'cái nhà', 1),
    ('DA2507', 'CH257', N'ăn',      0),
    ('DA2508', 'CH257', N'vui',     0),

    -- CH258: Động từ? (mở, to, chó, đỏ) -> "mở"
    ('DA2509', 'CH258', N'mở',  1),
    ('DA2510', 'CH258', N'chó', 0),
    ('DA2511', 'CH258', N'đỏ',  0),
    ('DA2512', 'CH258', N'to',  0),

    -- CH259: Tính từ? (thơm, mèo, mở, chạy) -> "thơm"
    ('DA2513', 'CH259', N'mèo',  0),
    ('DA2514', 'CH259', N'thơm', 1),
    ('DA2515', 'CH259', N'mở',   0),
    ('DA2516', 'CH259', N'chạy', 0),

    -- CH260: Từ viết đúng? (giờ, gio~, giơ~, giờ~) -> "giờ"
    ('DA2517', 'CH260', N'gio~', 0),
    ('DA2518', 'CH260', N'giơ~', 0),
    ('DA2519', 'CH260', N'giờ',  1),
    ('DA2520', 'CH260', N'giờ~', 0),


-- MỨC NÂNG CAO

    -- CH266: Từ viết đúng? (cánh, cãnh~, cáng~, cánh~) -> "cánh"
    ('DA2521', 'CH266', N'cãnh~', 0),
    ('DA2522', 'CH266', N'cánh',  1),
    ('DA2523', 'CH266', N'cáng~', 0),
    ('DA2524', 'CH266', N'cánh~', 0),

    -- CH267: Tính từ? (sáng, chạy, ăn, bàn) -> "sáng"
    ('DA2525', 'CH267', N'chạy', 0),
    ('DA2526', 'CH267', N'sáng', 1),
    ('DA2527', 'CH267', N'bàn',  0),
    ('DA2528', 'CH267', N'ăn',   0),

    -- CH268: "Bố đang ___ xe." -> "lái"
    ('DA2529', 'CH268', N'ăn',   0),
    ('DA2530', 'CH268', N'lái',  1),
    ('DA2531', 'CH268', N'vẽ',   0),
    ('DA2532', 'CH268', N'ngủ',  0),

    -- CH269: Động từ? (bơi, mèo, to, đẹp) -> "bơi"
    ('DA2533', 'CH269', N'mèo',  0),
    ('DA2534', 'CH269', N'to',   0),
    ('DA2535', 'CH269', N'bơi',  1),
    ('DA2536', 'CH269', N'đẹp',  0),

    -- CH270: Danh từ? (cái bút, xanh, vui, ngồi) -> "cái bút"
    ('DA2537', 'CH270', N'xanh',    0),
    ('DA2538', 'CH270', N'cái bút', 1),
    ('DA2539', 'CH270', N'vui',     0),
    ('DA2540', 'CH270', N'ngồi',    0),

    -- CH276: "Con mèo đang ___ bóng." -> "đuổi"
    ('DA2541', 'CH276', N'ăn',   0),
    ('DA2542', 'CH276', N'đuổi', 1),
    ('DA2543', 'CH276', N'nhảy', 0),
    ('DA2544', 'CH276', N'đọc',  0),

    -- CH277: Động từ? (nhảy, ghế, to, cao) -> "nhảy"
    ('DA2545', 'CH277', N'ghế',  0),
    ('DA2546', 'CH277', N'nhảy', 1),
    ('DA2547', 'CH277', N'to',   0),
    ('DA2548', 'CH277', N'cao',  0),

    -- CH278: Tính từ? (lạnh, mèo, mở, nhìn) -> "lạnh"
    ('DA2549', 'CH278', N'mèo',  0),
    ('DA2550', 'CH278', N'mở',   0),
    ('DA2551', 'CH278', N'lạnh', 1),
    ('DA2552', 'CH278', N'nhìn', 0),

    -- CH279: Danh từ? (con chó, chạy, xanh, buồn) -> "con chó"
    ('DA2553', 'CH279', N'chạy',    0),
    ('DA2554', 'CH279', N'con chó', 1),
    ('DA2555', 'CH279', N'xanh',    0),
    ('DA2556', 'CH279', N'buồn',    0),

    -- CH280: Từ đúng chính tả? (lạnh, lảnh~, lanh~, lănh~) -> "lạnh"
    ('DA2557', 'CH280', N'lảnh~', 0),
    ('DA2558', 'CH280', N'lạnh',  1),
    ('DA2559', 'CH280', N'lănh~', 0),
    ('DA2560', 'CH280', N'lanh~', 0),

    -- CH286: "Bạn nhỏ đang ___ bánh." -> "ăn"
    ('DA2561', 'CH286', N'vẽ',   0),
    ('DA2562', 'CH286', N'ăn',   1),
    ('DA2563', 'CH286', N'ném',  0),
    ('DA2564', 'CH286', N'giấu', 0),

    -- CH287: Động từ? (leo, đẹp, mèo, xanh) -> "leo"
    ('DA2565', 'CH287', N'leo',  1),
    ('DA2566', 'CH287', N'đẹp',  0),
    ('DA2567', 'CH287', N'mèo',  0),
    ('DA2568', 'CH287', N'xanh', 0),

    -- CH288: Tính từ? (sạch, con mèo, ngồi, chạy) -> "sạch"
    ('DA2569', 'CH288', N'con mèo', 0),
    ('DA2570', 'CH288', N'sạch',    1),
    ('DA2571', 'CH288', N'ngồi',    0),
    ('DA2572', 'CH288', N'chạy',    0),

    -- CH289: Danh từ? (cái thước, vui, ăn, to) -> "cái thước"
    ('DA2573', 'CH289', N'cái thước', 1),
    ('DA2574', 'CH289', N'ăn',        0),
    ('DA2575', 'CH289', N'to',        0),
    ('DA2576', 'CH289', N'vui',       0),

    -- CH290: Từ viết đúng? (bàn, ban~, bằn, bàn~) -> "bàn"
    ('DA2577', 'CH290', N'ban~', 0),
    ('DA2578', 'CH290', N'bàn',  1),
    ('DA2579', 'CH290', N'bằn',  0),
    ('DA2580', 'CH290', N'bàn~', 0),

--------------------------------------------------
    --PHHẦN THỬ THÁCH
--------------------------------------------------
-- Toán
----------------------------------------------------------
-- ĐÁP ÁN CH291 → CH310 (PHIÊN BẢN DỄ CHO LỚP 2–3)
-- Bắt đầu từ ID: DA2581
----------------------------------------------------------

-- CH291: 7 + 9 = 16
('DA2581', 'CH291', N'14', 0),
('DA2582', 'CH291', N'18', 0),
('DA2583', 'CH291', N'16', 1),
('DA2584', 'CH291', N'15', 0),


-- CH292: 18 ÷ 3 = 6
('DA2585', 'CH292', N'6', 1),
('DA2586', 'CH292', N'5', 0),
('DA2587', 'CH292', N'9', 0),
('DA2588', 'CH292', N'4', 0),


-- CH293: 45 - 16 = 29
('DA2589', 'CH293', N'30', 0),
('DA2590', 'CH293', N'26', 0),
('DA2591', 'CH293', N'25', 0),
('DA2592', 'CH293', N'29', 1),


-- CH294: 6 × 3 = 18
('DA2593', 'CH294', N'12', 0),
('DA2594', 'CH294', N'18', 1),
('DA2595', 'CH294', N'20', 0),
('DA2596', 'CH294', N'15', 0),


-- CH295: 12 + 8 = 20
('DA2597', 'CH295', N'22', 0),
('DA2598', 'CH295', N'18', 0),
('DA2599', 'CH295', N'25', 0),
('DA2600', 'CH295', N'20', 1),


-- CH296: 20 - 6 = 14
('DA2601', 'CH296', N'12', 0),
('DA2602', 'CH296', N'14', 1),
('DA2603', 'CH296', N'10', 0),
('DA2604', 'CH296', N'16', 0),


-- CH297: 9 × 4 = 36
('DA2605', 'CH297', N'36', 1),
('DA2606', 'CH297', N'40', 0),
('DA2607', 'CH297', N'32', 0),
('DA2608', 'CH297', N'30', 0),

-- CH298: 50 ÷ 5 = 10
('DA2609', 'CH298', N'12', 0),
('DA2610', 'CH298', N'8', 0),
('DA2611', 'CH298', N'10', 1),
('DA2612', 'CH298', N'15', 0),


-- CH299: 15 + 9 = 24
('DA2613', 'CH299', N'24', 1),
('DA2614', 'CH299', N'26', 0),
('DA2615', 'CH299', N'22', 0),
('DA2616', 'CH299', N'20', 0),

-- CH300: 8 × 3 = 24
('DA2617', 'CH300', N'20', 0),
('DA2618', 'CH300', N'18', 0),
('DA2619', 'CH300', N'30', 0),
('DA2620', 'CH300', N'24', 1),


-- CH301: 10 - 5 = 5
('DA2621', 'CH301', N'4', 0),
('DA2622', 'CH301', N'3', 0),
('DA2623', 'CH301', N'5', 1),
('DA2624', 'CH301', N'6', 0),


-- CH302: 36 ÷ 3 = 12
('DA2625', 'CH302', N'10', 0),
('DA2626', 'CH302', N'12', 1),
('DA2627', 'CH302', N'15', 0),
('DA2628', 'CH302', N'9', 0),


-- CH303: 27 - 9 = 18
('DA2629', 'CH303', N'18', 1),
('DA2630', 'CH303', N'16', 0),
('DA2631', 'CH303', N'20', 0),
('DA2632', 'CH303', N'19', 0),


-- CH304: 7 × 5 = 35
('DA2633', 'CH304', N'30', 0),
('DA2634', 'CH304', N'40', 0),
('DA2635', 'CH304', N'35', 1),
('DA2636', 'CH304', N'25', 0),


-- CH305: 32 ÷ 4 = 8
('DA2637', 'CH305', N'6', 0),
('DA2638', 'CH305', N'10', 0),
('DA2639', 'CH305', N'12', 0),
('DA2640', 'CH305', N'8', 1),


-- CH306: 14 + 6 = 20
('DA2641', 'CH306', N'22', 0),
('DA2642', 'CH306', N'20', 1),
('DA2643', 'CH306', N'25', 0),
('DA2644', 'CH306', N'18', 0),


-- CH307: 18 + 12 = 30
('DA2645', 'CH307', N'28', 0),
('DA2646', 'CH307', N'26', 0),
('DA2647', 'CH307', N'30', 1),
('DA2648', 'CH307', N'24', 0),


-- CH308: 42 ÷ 7 = 6
('DA2649', 'CH308', N'6', 1),
('DA2650', 'CH308', N'5', 0),
('DA2651', 'CH308', N'7', 0),
('DA2652', 'CH308', N'9', 0),

-- CH309: 9 × 2 = 18
('DA2653', 'CH309', N'20', 0),
('DA2654', 'CH309', N'22', 0),
('DA2655', 'CH309', N'16', 0),
('DA2656', 'CH309', N'18', 1),


-- CH310: 25 - 5 = 20
('DA2657', 'CH310', N'18', 0),
('DA2658', 'CH310', N'20', 1),
('DA2659', 'CH310', N'22', 0),
('DA2660', 'CH310', N'15', 0),

-- Tiếng Việt
----------------------------------------------------------
-- ĐÁP ÁN TV CH311 → CH330
-- Bắt đầu từ ID: DA2661
----------------------------------------------------------

-- CH311: Động từ trong câu là: "đọc"
('DA2661', 'CH311', N'Lan', 0),
('DA2662', 'CH311', N'đọc', 1),
('DA2663', 'CH311', N'Bé', 0),
('DA2664', 'CH311', N'sách', 0),


-- CH312: Bạn nhỏ rất ___ → "vui"
('DA2665', 'CH312', N'vui', 1),
('DA2666', 'CH312', N'đọc', 0),
('DA2667', 'CH312', N'chạy', 0),
('DA2668', 'CH312', N'cao', 0),


-- CH313: Trái nghĩa với "mạnh" → "yếu"
('DA2669', 'CH313', N'nhanh', 0),
('DA2670', 'CH313', N'yếu', 1),
('DA2671', 'CH313', N'cao', 0),
('DA2672', 'CH313', N'đẹp', 0),


-- CH314: Từ viết đúng: "khỏe"
('DA2673', 'CH314', N'khoẽ', 0),
('DA2674', 'CH314', N'khỏe', 1),
('DA2675', 'CH314', N'khỏe~', 0),
('DA2676', 'CH314', N'khoẻ~', 0),


-- CH315: Thời tiết thật ___ → "đẹp"
('DA2677', 'CH315', N'đẹp', 1),
('DA2678', 'CH315', N'bé', 0),
('DA2679', 'CH315', N'chạy', 0),
('DA2680', 'CH315', N'bàn', 0),


-- CH316: Danh từ → "con đường"
('DA2681', 'CH316', N'ngủ', 0),
('DA2682', 'CH316', N'con đường', 1),
('DA2683', 'CH316', N'cao', 0),
('DA2684', 'CH316', N'ăn', 0),


-- CH317: "chuẩn bị" là động từ
('DA2685', 'CH317', N'tính từ', 0),
('DA2686', 'CH317', N'đại từ', 0),
('DA2687', 'CH317', N'động từ', 1),
('DA2688', 'CH317', N'danh từ', 0),


-- CH318: Cố gắng ___ bài toán → "giải"
('DA2689', 'CH318', N'đẹp', 0),
('DA2690', 'CH318', N'xanh', 0),
('DA2691', 'CH318', N'ăn', 0),
('DA2692', 'CH318', N'giải', 1),


-- CH319: Danh từ trong câu là: "ông", "cây"
-- Chọn danh từ phù hợp nhất → "ông"
('DA2693', 'CH319', N'em', 0),
('DA2694', 'CH319', N'tưới', 0),
('DA2695', 'CH319', N'ông', 1),
('DA2696', 'CH319', N'đang', 0),


-- CH320: Bé Hoa rất ___ → "vui"
('DA2697', 'CH320', N'vui', 1),
('DA2698', 'CH320', N'cao', 0),
('DA2699', 'CH320', N'ăn', 0),
('DA2700', 'CH320', N'chạy', 0),


-- CH321: Trái nghĩa của "cao" → "thấp"
('DA2701', 'CH321', N'nóng', 0),
('DA2702', 'CH321', N'thấp', 1),
('DA2703', 'CH321', N'nhanh', 0),
('DA2704', 'CH321', N'đẹp', 0),


-- CH322: Từ đúng chính tả → "nghỉ ngơi"
('DA2705', 'CH322', N'ngỉ ngơi', 0),
('DA2706', 'CH322', N'ngĩ ngơi', 0),
('DA2707', 'CH322', N'nghỉ ngơi', 1),
('DA2708', 'CH322', N'nghỉ ngơi~', 0),


-- CH323: Cả lớp đang ___ bài hát → "hát"
('DA2709', 'CH323', N'vui', 0),
('DA2710', 'CH323', N'hát', 1),
('DA2711', 'CH323', N'đỏ', 0),
('DA2712', 'CH323', N'chạy', 0),


-- CH324: Tính từ → "đẹp"
('DA2713', 'CH324', N'ăn', 0),
('DA2714', 'CH324', N'đẹp', 1),
('DA2715', 'CH324', N'bút', 0),
('DA2716', 'CH324', N'mèo', 0),


-- CH325: "chăm chỉ" là tính từ
('DA2717', 'CH325', N'tính từ', 1),
('DA2718', 'CH325', N'thán từ', 0),
('DA2719', 'CH325', N'danh từ', 0),
('DA2720', 'CH325', N'động từ', 0),


-- CH326: Chiếc áo này rất ___ → "đẹp"
('DA2721', 'CH326', N'ngủ', 0),
('DA2722', 'CH326', N'chạy', 0),
('DA2723', 'CH326', N'đẹp', 1),
('DA2724', 'CH326', N'uống', 0),


-- CH327: Trái nghĩa của "buồn" → "vui"
('DA2725', 'CH327', N'vui', 1),
('DA2726', 'CH327', N'thấp', 0),
('DA2727', 'CH327', N'to', 0),
('DA2728', 'CH327', N'mới', 0),


-- CH328: "nấu" là động từ
('DA2729', 'CH328', N'danh từ', 0),
('DA2730', 'CH328', N'động từ', 1),
('DA2731', 'CH328', N'tính từ', 0),
('DA2732', 'CH328', N'thán từ', 0),


-- CH329: Bạn nhỏ ___ tay chào → "vẫy"
('DA2733', 'CH329', N'xanh', 0),
('DA2734', 'CH329', N'đẹp', 0),
('DA2735', 'CH329', N'ăn', 0),
('DA2736', 'CH329', N'vẫy', 1),


-- CH330: Danh từ → "quyển sách"
('DA2737', 'CH330', N'đỏ', 0),
('DA2738', 'CH330', N'quyển sách', 1),
('DA2739', 'CH330', N'vui', 0),
('DA2740', 'CH330', N'chạy', 0),



--------------------------------------------------
    -- TRÒ CHƠI
--------------------------------------------------
	
	-- CHG001: Bé đang ___ bóng. (đúng: đá)
    ('DA3033', 'CHG001', N'ăn',   0),
    ('DA3034', 'CHG001', N'đá',   1),
    ('DA3035', 'CHG001', N'ngủ',  0),
    ('DA3036', 'CHG001', N'vẽ',   0),

    -- CHG002: Con mèo ___ trên sân. (đúng: nằm)
    ('DA3037', 'CHG002', N'bay',  0),
    ('DA3038', 'CHG002', N'bơi',  0),
    ('DA3039', 'CHG002', N'nằm',  1),
    ('DA3040', 'CHG002', N'đọc',  0),

    -- CHG003: Bạn nhỏ rất ___ khi được khen. (đúng: vui)
    ('DA3041', 'CHG003', N'vui',  1),
    ('DA3042', 'CHG003', N'buồn', 0),
    ('DA3043', 'CHG003', N'đói',  0),
    ('DA3044', 'CHG003', N'mệt',  0),

    -- CHG004: Mẹ mua cho em một chiếc ___ mới. (đúng: áo)
    ('DA3045', 'CHG004', N'chó',  0),
    ('DA3046', 'CHG004', N'nước', 0),
    ('DA3047', 'CHG004', N'táo',  0),
    ('DA3048', 'CHG004', N'áo',   1),

    -- CHG005: Bố đang ___ xe máy. (đúng: lái)
    ('DA3049', 'CHG005', N'ăn',   0),
    ('DA3050', 'CHG005', N'lái',  1),
    ('DA3051', 'CHG005', N'ngủ',  0),
    ('DA3052', 'CHG005', N'bơi',  0),

    -- CHG006: Em bé đang ___ sữa. (đúng: uống)
    ('DA3053', 'CHG006', N'chạy', 0),
    ('DA3054', 'CHG006', N'vẽ',   0),
    ('DA3055', 'CHG006', N'uống', 1),
    ('DA3056', 'CHG006', N'bay',  0),

    -- CHG007: Ông nội đang ___ báo. (đúng: đọc)
    ('DA3057', 'CHG007', N'đọc',  1),
    ('DA3058', 'CHG007', N'hát',  0),
    ('DA3059', 'CHG007', N'múa',  0),
    ('DA3060', 'CHG007', N'nhảy', 0),

    -- CHG008: Cô giáo đang ___ bài. (đúng: giảng)
    ('DA3061', 'CHG008', N'ngủ',  0),
    ('DA3062', 'CHG008', N'bơi',  0),
    ('DA3063', 'CHG008', N'chơi', 0),
    ('DA3064', 'CHG008', N'giảng',1),

    -- CHG009: Bé Lan ___ tóc cho em gái. (đúng: chải)
    ('DA3065', 'CHG009', N'ăn',   0),
    ('DA3066', 'CHG009', N'chải', 1),
    ('DA3067', 'CHG009', N'uống', 0),
    ('DA3068', 'CHG009', N'chạy', 0),

    -- CHG010: Trời hôm nay rất ___. (đúng: đẹp)
    ('DA3069', 'CHG010', N'vui',  0),
    ('DA3070', 'CHG010', N'đói',  0),
    ('DA3071', 'CHG010', N'đẹp',  1),
    ('DA3072', 'CHG010', N'mệt',  0),

    -- CHG011: Bạn Nam đang ___ nhà. (đúng: quét)
    ('DA3073', 'CHG011', N'quét', 1),
    ('DA3074', 'CHG011', N'ăn',   0),
    ('DA3075', 'CHG011', N'ngủ',  0),
    ('DA3076', 'CHG011', N'uống', 0),

    -- CHG012: Con chim đang ___ trên cành. (đúng: hót)
    ('DA3077', 'CHG012', N'bơi',  0),
    ('DA3078', 'CHG012', N'chạy', 0),
    ('DA3079', 'CHG012', N'đọc',  0),
    ('DA3080', 'CHG012', N'hót',  1),

    -- CHG013: Em đặt cặp lên ___. (đúng: bàn)
    ('DA3081', 'CHG013', N'tường', 0),
    ('DA3082', 'CHG013', N'bàn',   1),
    ('DA3083', 'CHG013', N'trần',  0),
    ('DA3084', 'CHG013', N'mây',   0),

    -- CHG014: Bé đi học mang theo một cái ___. (đúng: cặp)
    ('DA3085', 'CHG014', N'tivi',  0),
    ('DA3086', 'CHG014', N'giường',0),
    ('DA3087', 'CHG014', N'cặp',   1),
    ('DA3088', 'CHG014', N'tủ',    0),

    -- CHG015: Bạn nhỏ đang ___ tranh. (đúng: vẽ)
    ('DA3089', 'CHG015', N'vẽ',   1),
    ('DA3090', 'CHG015', N'ăn',   0),
    ('DA3091', 'CHG015', N'uống', 0),
    ('DA3092', 'CHG015', N'hát',  0),

    -- CHG016: Em phải ___ tay trước khi ăn. (đúng: rửa)
    ('DA3093', 'CHG016', N'cắt',  0),
    ('DA3094', 'CHG016', N'vẽ',   0),
    ('DA3095', 'CHG016', N'che',  0),
    ('DA3096', 'CHG016', N'rửa',  1),

    -- CHG017: Con chó chạy ___ sân. (đúng: quanh)
    ('DA3097', 'CHG017', N'cao',   0),
    ('DA3098', 'CHG017', N'quanh', 1),
    ('DA3099', 'CHG017', N'đẹp',   0),
    ('DA3100', 'CHG017', N'buồn',  0),

    -- CHG018: Bé cười rất ___. (đúng: tươi)
    ('DA3101', 'CHG018', N'mệt',  0),
    ('DA3102', 'CHG018', N'đói',  0),
    ('DA3103', 'CHG018', N'tươi', 1),
    ('DA3104', 'CHG018', N'khát', 0),

    -- CHG019: Bạn nhỏ đang ___ bánh. (đúng: ăn)
    ('DA3105', 'CHG019', N'ăn',   1),
    ('DA3106', 'CHG019', N'đá',   0),
    ('DA3107', 'CHG019', N'viết', 0),
    ('DA3108', 'CHG019', N'bơi',  0),

    -- CHG020: Mẹ đang chuẩn bị ___ cho cả nhà. (đúng: bữa cơm)
    ('DA3109', 'CHG020', N'tivi',    0),
    ('DA3110', 'CHG020', N'xe đạp',  0),
    ('DA3111', 'CHG020', N'bóng',    0),
    ('DA3112', 'CHG020', N'bữa cơm', 1);

GO

USE UngDungHocTapChoTre;
GO

DECLARE @MaMH_Toan char(5) = (SELECT MaMonHoc FROM MonHoc WHERE TenMonHoc = N'Toán');
DECLARE @MaMH_TV   char(5) = (SELECT MaMonHoc FROM MonHoc WHERE TenMonHoc = N'Tiếng Việt');
DECLARE @MaLoai_TroChoi char(5) = (SELECT MaLoai FROM LoaiHoatDong WHERE TenLoai = N'Trò Chơi');

-- 3 trò chơi:
-- TC001: Liên hoàn tính toán (lấy random từ Ôn luyện trong code, không cần map cứng)
-- TC002: Hoàn thiện câu từ  (map CHG001–CHG020)
-- TC003: Trùm tính nhẩm    (map CHG021–CHG040)

IF NOT EXISTS (SELECT 1 FROM HoatDongHocTap WHERE MaHoatDong = 'TC001')
BEGIN
    INSERT INTO HoatDongHocTap (MaHoatDong, MaMonHoc, MaLoai, TieuDe, MoTa, TongDiemToiDa) VALUES
    ('TC001', @MaMH_Toan, @MaLoai_TroChoi, N'Liên hoàn tính toán', N'Trò chơi tính toán ngẫu nhiên từ phần Ôn luyện.', 100),
    ('TC002', @MaMH_TV  , @MaLoai_TroChoi, N'Hoàn thiện câu từ',   N'Trò chơi điền từ vào chỗ trống.', 50),
    ('TC003', @MaMH_Toan, @MaLoai_TroChoi, N'Trùm tính nhẩm',      N'Trò chơi tính nhẩm nhanh nhiều phép tính.', 50);
END
GO

-- CỦNG CỐ TOÁN (CC001–CC010): CH001–CH100
DECLARE @ccToan INT, @qToan INT, @tToan INT;

SET @ccToan = 1;   -- CC001
SET @qToan  = 1;   -- CH001

WHILE @ccToan <= 10
BEGIN
    SET @tToan = 1;
    WHILE @tToan <= 10
    BEGIN
        INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
        VALUES (
            'CC' + RIGHT('000' + CAST(@ccToan AS VARCHAR(3)), 3),  -- CC001..CC010
            'CH' + RIGHT('000' + CAST(@qToan  AS VARCHAR(3)), 3),  -- CH001..CH100
            @tToan
        );

        SET @qToan  = @qToan + 1;
        SET @tToan  = @tToan + 1;
    END;

    SET @ccToan = @ccToan + 1;
END;
GO
-- CỦNG CỐ TIẾNG VIỆT (CC011–CC020): CH101–CH200
DECLARE @ccTV INT, @qTV INT, @tTV INT;

SET @ccTV = 11;   -- CC011
SET @qTV  = 101;  -- CH101

WHILE @ccTV <= 20
BEGIN
    SET @tTV = 1;
    WHILE @tTV <= 10
    BEGIN
        INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
        VALUES (
            'CC' + RIGHT('000' + CAST(@ccTV AS VARCHAR(3)), 3),   -- CC011..CC020
            'CH' + RIGHT('000' + CAST(@qTV  AS VARCHAR(3)), 3),   -- CH101..CH200
            @tTV
        );

        SET @qTV  = @qTV + 1;
        SET @tTV  = @tTV + 1;
    END;

    SET @ccTV = @ccTV + 1;
END;
GO
-- ÔN LUYỆN (OL001–OL009): CH201–CH290
DECLARE @ol INT, @qOL INT, @tOL INT;

SET @ol  = 1;    -- OL001
SET @qOL = 201;  -- CH201

WHILE @ol <= 9
BEGIN
    SET @tOL = 1;
    WHILE @tOL <= 10
    BEGIN
        INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
        VALUES (
            'OL' + RIGHT('000' + CAST(@ol  AS VARCHAR(3)), 3),   -- OL001..OL009
            'CH' + RIGHT('000' + CAST(@qOL AS VARCHAR(3)), 3),   -- CH201..CH290
            @tOL
        );

        SET @qOL = @qOL + 1;
        SET @tOL = @tOL + 1;
    END;

    SET @ol = @ol + 1;
END;
GO
----------------------------------------------------------
-- GÁN CÂU HỎI TIẾNG VIỆT CH311–CH330 CHO TT001
----------------------------------------------------------
DECLARE @qTV INT = 311;  -- CH311
DECLARE @tTV INT = 1;    -- Thứ tự 1..20

WHILE @tTV <= 20
BEGIN
    INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
    VALUES (
        'TT001',                                                -- Thử thách Tiếng Việt
        'CH' + RIGHT('000' + CAST(@qTV AS VARCHAR(3)), 3),      -- CH311..CH330
        @tTV                                                    -- Thứ tự 1..20
    );

    SET @qTV = @qTV + 1;
    SET @tTV = @tTV + 1;
END;


----------------------------------------------------------
-- GÁN CÂU HỎI TOÁN CH291–CH310 CHO TT002
----------------------------------------------------------
DECLARE @qT INT = 291;   -- CH291
DECLARE @tT INT = 1;     -- Thứ tự 1..20

WHILE @tT <= 20
BEGIN
    INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
    VALUES (
        'TT002',                                                -- Thử thách Toán
        'CH' + RIGHT('000' + CAST(@qT AS VARCHAR(3)), 3),       -- CH291..CH310
        @tT                                                     -- Thứ tự 1..20
    );

    SET @qT = @qT + 1;
    SET @tT = @tT + 1;
END;


-- Ví dụ query cho Liên hoàn tính toán (TC001) trong ứng dụng:
-- Lấy 10 câu ngẫu nhiên từ các câu Ôn luyện Toán CH201–CH290
SELECT TOP 10
    CH.MaCauHoi,
    CH.NoiDungCauHoi,
    DAP.MaDapAn,
    DAP.NoiDungDapAn,
    DAP.LaDapAnDung
FROM CauHoi CH
JOIN DapAn DAP ON CH.MaCauHoi = DAP.MaCauHoi
WHERE CH.MaCauHoi BETWEEN 'CH201' AND 'CH290'
ORDER BY NEWID();
-- TC002: Hoàn thiện câu từ → CHG001–CHG020
DECLARE @g1 INT = 1, @tG1 INT = 1;

WHILE @tG1 <= 20
BEGIN
    INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
    VALUES (
        'TC002',
        'CHG' + RIGHT('000' + CAST(@g1 AS VARCHAR(3)), 3),  -- CHG001..CHG020
        @tG1
    );

    SET @g1  = @g1 + 1;
    SET @tG1 = @tG1 + 1;
END;
GO
-- TC003: Trùm tính nhẩm → CHG021–CHG040
DECLARE @g2 INT = 21, @tG2 INT = 1;

WHILE @tG2 <= 20
BEGIN
    INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
    VALUES (
        'TC003',
        'CHG' + RIGHT('000' + CAST(@g2 AS VARCHAR(3)), 3),  -- CHG021..CHG040
        @tG2
    );

    SET @g2  = @g2 + 1;
    SET @tG2 = @tG2 + 1;
END;
GO



select* from hoatdonghoctap
select* from LoaiHoatDong
  --củng cố
  DECLARE @maNguoiDung NVARCHAR(50) = 'ND007';
SELECT COUNT(DISTINCT h.MaHoatDong) AS SoDeCoBanDaLam
FROM TienTrinhHocTap t
JOIN HoatDongHocTap h ON t.MaHoatDong = h.MaHoatDong
WHERE t.MaNguoiDung = @maNguoiDung
  AND h.TieuDe LIKE N'Ôn cơ bản%';

SELECT COUNT(*) AS TongSoDeCoBan
FROM HoatDongHocTap
WHERE TieuDe LIKE N'Ôn cơ bản%';

DECLARE @maNguoiDung1 NVARCHAR(50) = 'ND007';
SELECT COUNT(DISTINCT h.MaHoatDong) AS SoDeTrungBinhDaLam
FROM TienTrinhHocTap t
JOIN HoatDongHocTap h ON t.MaHoatDong = h.MaHoatDong
WHERE t.MaNguoiDung = @maNguoiDung1
  AND h.TieuDe LIKE N'Ôn TB%';


SELECT COUNT(*) AS TongSoDeTrungBinh
FROM HoatDongHocTap
WHERE TieuDe LIKE N'Ôn TB%';

DECLARE @maNguoiDung2 NVARCHAR(50) = 'ND007';
SELECT COUNT(DISTINCT h.MaHoatDong) AS SoDeNangCaoDaLam
FROM TienTrinhHocTap t
JOIN HoatDongHocTap h ON t.MaHoatDong = h.MaHoatDong
WHERE t.MaNguoiDung = @maNguoiDung2
  AND h.TieuDe LIKE N'Ôn NC%';

SELECT COUNT(*) AS TongSoDeNangCao
FROM HoatDongHocTap
WHERE TieuDe LIKE N'Ôn NC%';

----------------------------insert ôn luyện---------------------------------
GO
-- b. OnLuyen (OL001 - OL009) -> Sử dụng CH201 - CH290
DECLARE @ol INT = 10, @ch INT = 01;  -- bắt đầu từ CH001
WHILE @ol <= 30
BEGIN
    DECLARE @thuTu INT = 1;

    WHILE @thuTu <= 10
    BEGIN
        INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
        VALUES (
            'OL' + RIGHT('000' + CAST(@ol AS VARCHAR(3)), 3),
            'CH' + RIGHT('000' + CAST(@ch AS VARCHAR(3)), 3),
            @thuTu
        );

        SET @ch = @ch + 1;  
        SET @thuTu = @thuTu + 1; 
    END

    SET @ol = @ol + 1;  
END
GO
SELECT 
    h.MaHoatDong,
    h.TieuDe,
    c.MaCauHoi,
    c.DiemToiDa,
    c.NoiDungCauHoi AS CauHoi,
    d.MaDapAn,
    d.NoiDungDapAn AS DapAn,
    d.LaDapAnDung
FROM HoatDongHocTap h
JOIN HoatDong_CauHoi hc 
    ON h.MaHoatDong = hc.MaHoatDong
JOIN CauHoi c 
    ON hc.MaCauHoi = c.MaCauHoi
JOIN DapAn d 
    ON c.MaCauHoi = d.MaCauHoi
WHERE h.TieuDe = N'Ôn TB 1'
ORDER BY c.MaCauHoi, d.MaDapAn;

SELECT
    HD.MaHoatDong, HD.TieuDe,
    CH.MaCauHoi, CH.NoiDungCauHoi
FROM HoatDongHocTap HD
JOIN HoatDong_CauHoi HQ ON HD.MaHoatDong = HQ.MaHoatDong
JOIN CauHoi CH ON HQ.MaCauHoi = CH.MaCauHoi
WHERE HD.MaLoai = 'LHD02' AND HD.MaMonHoc = 'MH002'
ORDER BY HD.MaHoatDong, HQ.ThuTu;


SELECT 
    h.MaHoatDong,
    h.TieuDe,
    c.MaCauHoi,
    c.DiemToiDa,
    c.NoiDungCauHoi AS CauHoi,
    d.MaDapAn,
    d.NoiDungDapAn AS DapAn,
    d.LaDapAnDung
FROM HoatDongHocTap h
JOIN HoatDong_CauHoi hc 
    ON h.MaHoatDong = hc.MaHoatDong
JOIN CauHoi c 
    ON hc.MaCauHoi = c.MaCauHoi
JOIN DapAn d 
    ON c.MaCauHoi = d.MaCauHoi
WHERE h.TieuDe = N'Ôn cơ bản 1'
ORDER BY c.MaCauHoi, d.MaDapAn;

SELECT CH.MaCauHoi,
       COUNT(DA.MaDapAn) AS SoDapAn
FROM CauHoi CH
LEFT JOIN DapAn DA ON CH.MaCauHoi = DA.MaCauHoi
GROUP BY CH.MaCauHoi
HAVING COUNT(DA.MaDapAn) <> 4
ORDER BY CH.MaCauHoi;


--Kiểm tra về câu hỏi và đáp án 
SELECT COUNT(*) FROM CauHoi;   -- phải = 350
SELECT COUNT(*) FROM DapAn;    -- phải = 1400
SELECT MIN(MaCauHoi), MAX(MaCauHoi) FROM CauHoi;   -- CH001 -> CH350
SELECT MIN(MaDapAn), MAX(MaDapAn) FROM DapAn;      -- DA0001 -> DA1400

select * from hoatdonghoctap
Select * from CauHoi
select * from DapAn
select * from HoatDong_CauHoi
select * from LoaiHoatDong
select * from TienTrinhHocTap
SELECT 
    h.MaHoatDong,
    h.TieuDe,
    c.MaCauHoi,
    c.DiemToiDa,
    c.NoiDungCauHoi AS CauHoi,
    d.MaDapAn,
    d.NoiDungDapAn AS DapAn,
    d.LaDapAnDung
FROM HoatDongHocTap h
JOIN HoatDong_CauHoi hc 
    ON h.MaHoatDong = hc.MaHoatDong
JOIN CauHoi c 
    ON hc.MaCauHoi = c.MaCauHoi
JOIN DapAn d 
    ON c.MaCauHoi = d.MaCauHoi
WHERE h.TieuDe = N'Hoàn thiện câu từ'
ORDER BY c.MaCauHoi, d.MaDapAn;