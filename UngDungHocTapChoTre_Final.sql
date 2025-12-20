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
    MoTa NVARCHAR(500) NULL,
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

UPDATE HoatDongHocTap SET MoTa = 
N'Trong bài học này, bé sẽ được làm quen với phép cộng thông qua những tình huống gần gũi. Bé tưởng tượng mình có 3 quả táo 🍎 và được tặng thêm 2 quả táo nữa từ mẹ. Bé xếp các quả táo thành một hàng và đếm từng quả: 1, 2, 3, 4, 5. Vậy tổng cộng bé có 5 quả táo.
Bé cũng sẽ được thực hành cộng bằng ngón tay ✋, que tính hoặc hình minh họa để hiểu rằng phép cộng nghĩa là “gộp lại cho nhiều hơn”. Ví dụ thêm: 4 con cá 🐟 đang bơi, có thêm 1 con cá nữa bơi tới → 4 + 1 = 5.'
WHERE MaHoatDong = 'LT001';

UPDATE HoatDongHocTap SET MoTa = 
N'Bài học này giúp bé hiểu rằng phép trừ nghĩa là “lấy bớt đi”. Bé hình dung mình có 5 viên kẹo 🍬 và ăn mất 2 viên, bé đếm lại còn 3 viên. Bé sẽ được hướng dẫn dùng que tính, khối gỗ hoặc hình vẽ để “bỏ bớt” đi nhằm hiểu rõ phép trừ.
Ví dụ khác: Trên cành cây có 6 chú chim 🐦, 1 chú bay đi → còn 5 chú. Bé sẽ học cách đếm ngược và nhận ra phép trừ giúp biết còn lại bao nhiêu sau khi mất đi một phần.'
WHERE MaHoatDong = 'LT002';

UPDATE HoatDongHocTap SET MoTa = 
N'Trong bài học này, bé sẽ biết rằng phép nhân chính là cộng nhiều lần một số giống nhau. Bé quan sát 3 hộp quà 🎁, mỗi hộp có 2 ngôi sao 🌟. Khi bé đếm: 2 + 2 + 2 = 6, bé hiểu rằng 3 × 2 = 6.
Bé sẽ được xem thêm ví dụ: 4 chậu hoa 🌸, mỗi chậu có 3 bông → 4 × 3 = 12. Phép nhân giúp bé tính nhanh mà không cần cộng lặp lại quá nhiều. Bé cũng thử tự tạo “nhóm đồ vật” để luyện tập.'
WHERE MaHoatDong = 'LT003';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé được học rằng phép chia là “chia đều cho công bằng”. Ví dụ: có 7 chiếc bánh 🍪 chia cho 2 bạn. Bé sẽ chia lần lượt: mỗi bạn 1 cái, rồi 1 cái nữa… đến khi hết. Kết quả: mỗi bạn 3 cái và còn dư 1 cái. Bé hiểu rằng: 7 : 2 = 3 (dư 1).
Ví dụ khác: 9 cây bút ✏️ chia cho 3 bạn → mỗi bạn 3 cây. Bé sẽ tập chia đồ vật thật để hiểu sâu hơn việc phân chia đều nhau.'
WHERE MaHoatDong = 'LT004';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé làm quen với hình vuông 🟦 – một hình có 4 cạnh bằng nhau và 4 góc vuông. Bé quan sát hình vuông trong vở ô ly, viên gạch, chiếc khăn nhỏ…
Bài học giúp bé phân biệt hình vuông và hình chữ nhật bằng cách so sánh độ dài các cạnh. Bé còn được thực hành vẽ hình vuông bằng thước 📏 để rèn kỹ năng quan sát.'
WHERE MaHoatDong = 'LT005';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé tìm hiểu về các đơn vị đo quen thuộc. “Mét” 📏 dùng để đo chiều dài: chiều cao cái bàn, chiều dài thước, chiều dài căn phòng. “Kilôgam” ⚖️ dùng để đo cân nặng: túi gạo 1kg, quả dưa hấu 2kg.
Bé sẽ được thực hành đo chiều dài bằng thước mét và cân những đồ vật nhẹ bằng cân mini. Nhờ vậy, các đơn vị đo trở nên gần gũi và dễ nhớ hơn.'
WHERE MaHoatDong = 'LT006';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé học phân số thông qua việc chia đồ vật. Khi bé cắt chiếc bánh thành hai phần bằng nhau, mỗi phần được gọi là 1/2 🍰. Nếu chiếc bánh được chia thành 4 phần giống nhau, mỗi phần là 1/4.
Bé sẽ được làm bài tập chia bánh, pizza, thanh socola… để hiểu rằng phân số là “một phần của cái lớn hơn”. Bé cũng học cách đọc, viết và nhận dạng các phân số quen thuộc như 1/2, 1/3, 1/4.'
WHERE MaHoatDong = 'LT007';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé làm quen số thập phân bằng đoạn thẳng chia thành 10 phần bằng nhau 🌈. Bé hiểu rằng 0.1 là “một phần trong mười phần”, 0.2 là “hai phần”,…
Ví dụ: khi bé ghép 1 phần và 2 phần, bé được 3 phần → 0.1 + 0.2 = 0.3. Bé xem hình minh họa, tô màu từng phần để hiểu số thập phân dễ hơn.'
WHERE MaHoatDong = 'LT008';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé học cách đọc đồng hồ kim 🕒. Kim ngắn chỉ giờ, kim dài chỉ phút. Bé quan sát ví dụ: kim ngắn chỉ số 3, kim dài chỉ số 12 → 3 giờ.
Sau đó bé tập đọc nhiều thời gian khác: 7 giờ, 9 giờ rưỡi, 10 giờ kém 15. Bé cũng được xem đồng hồ điện tử để biết 7:30 nghĩa là 7 giờ 30 phút.'
WHERE MaHoatDong = 'LT009';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé nhận biết các loại tiền quen thuộc như 1.000đ, 2.000đ, 5.000đ 💵 và tập cộng chúng lại. Ví dụ: 5.000đ + 2.000đ = 7.000đ.
Bé đóng vai người mua hàng trong cửa hàng mini, chọn món đồ và tính số tiền phải trả. Bài học giúp bé biết giá trị của tiền và cách chi tiêu hợp lý.'
WHERE MaHoatDong = 'LT010';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé học 29 chữ cái bằng ví dụ trực quan. Ví dụ: A – quả táo 🍎, B – quả bóng ⚽, C – con cá 🐟. Bé luyện đọc từng chữ và nhận diện hình dạng chữ in – chữ viết.
Bé còn được “ghép chữ thành tiếng” như: B + a = Ba, C + o = Co, để làm quen bước đầu với đọc – viết.'
WHERE MaHoatDong = 'LT011';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé phân biệt ba vần a – ă – â qua những từ quen thuộc:

a → cá 🐟

ă → mắt 👁️

â → ấm nước ☕
Bé luyện phát âm đúng, đặt tay lên cổ họng để cảm nhận độ mở miệng khác nhau. Bé ghép các vần để tạo tiếng: la – lă – lâm.'
WHERE MaHoatDong = 'LT012';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé làm quen với các từ đơn quen thuộc: mẹ, bố, nhà 🏠, xe 🚗. Bé xem tranh minh họa và đọc từng từ.
Bé tập đặt câu đơn giản như: “Mẹ nấu cơm.”, “Xe màu đỏ.” để bước đầu làm quen với câu.'
WHERE MaHoatDong = 'LT013';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé học cách ghép hai tiếng tạo thành từ mới. Ví dụ: nhà + cửa = nhà cửa 🏠, học + sinh = học sinh 🎒.
Bé chơi trò “ghép thẻ từ” để tạo thêm nhiều từ mới. Bé còn được xem tranh để hiểu rõ nghĩa của từng từ ghép.'
WHERE MaHoatDong = 'LT014';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé hiểu rằng câu đơn chỉ diễn tả một hành động hoặc trạng thái. Ví dụ: “Bé Lan đi học.” 🎒
Bé quan sát tranh như con mèo 🐱 ngủ, chiếc xe chạy 🚗 rồi tập đặt câu: “Con mèo ngủ.”, “Xe chạy nhanh.” Bé học cách viết câu có đủ chủ ngữ và vị ngữ.'
WHERE MaHoatDong = 'LT015';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé làm quen với dấu câu như dấu chấm (.), dấu phẩy (,), dấu chấm hỏi (?). Ví dụ: “Con mèo ngủ.” 🐱; “Bé ăn cơm, uống nước.” 🍚. Bé tập sửa câu để dùng dấu đúng.'
WHERE MaHoatDong = 'LT016';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé học rằng có những từ phát âm giống nhau nhưng nghĩa khác nhau. Ví dụ: “lá” trong lá cây 🍃 và “lá” trong lá thư ✉️.
Bé xem tranh minh họa và nối đúng từ với nghĩa phù hợp. Nhờ vậy bé hiểu được sự phong phú của tiếng Việt.'
WHERE MaHoatDong = 'LT017';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé học các cặp từ trái nghĩa qua ví dụ sinh động: lớn – nhỏ 🎈🐭, cao – thấp 📏, nhanh – chậm 🏃🐢.
Bé chọn tranh tương ứng với từ và tập đặt câu để ghi nhớ rõ hơn: “Người cao.”, “Con rùa chậm.”.'
WHERE MaHoatDong = 'LT018';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé học thành ngữ qua ví dụ: “công cha, nghĩa mẹ” 👪. Bé nghe một câu chuyện ngắn và trả lời câu hỏi để hiểu ý nghĩa thành ngữ.'
WHERE MaHoatDong = 'LT019';

UPDATE HoatDongHocTap SET MoTa = 
N'Bé tìm hiểu thành ngữ qua câu ví dụ: “công cha, nghĩa mẹ” 👪. Bé nghe một câu chuyện ngắn kể về công lao của cha mẹ và trả lời câu hỏi để hiểu ý nghĩa tình cảm sâu sắc trong thành ngữ.'
WHERE MaHoatDong = 'LT020';


-- b. CungCo (CC001-CC020)
INSERT INTO HoatDongHocTap (MaHoatDong, MaMonHoc, MaLoai, TieuDe, MoTa, TongDiemToiDa) VALUES
('CC001', @MaMH_Toan, @MaLoai_CC, N'Củng cố Phép cộng', N'Luyện tập phép cộng', 50),
('CC002', @MaMH_Toan, @MaLoai_CC, N'Củng cố Phép trừ', N'Luyện tập phép trừ', 50),
('CC003', @MaMH_Toan, @MaLoai_CC, N'Củng cố Phép nhân', N'Luyện tập bảng nhân', 50),
('CC004', @MaMH_Toan, @MaLoai_CC, N'Củng cố Phép chia', N'Luyện tập phép chia', 50),
('CC005', @MaMH_Toan, @MaLoai_CC, N'Củng cố Hình học', N'Luyện tập hình học cơ bản', 50),
('CC006', @MaMH_Toan, @MaLoai_CC, N'Củng cố Đơn vị', N'Luyện tập đơn vị đo', 50),
('CC007', @MaMH_Toan, @MaLoai_CC, N'Củng cố Phân số', N'Luyện tập phân số', 50),
('CC008', @MaMH_Toan, @MaLoai_CC, N'Củng cố Thập phân', N'Luyện tập số thập phân', 50),
('CC009', @MaMH_Toan, @MaLoai_CC, N'Củng cố Thời gian', N'Luyện đọc và tính thời gian', 50),
('CC010', @MaMH_Toan, @MaLoai_CC, N'Củng cố Tiền tệ', N'Luyện tập tính tiền', 50),
('CC011', @MaMH_TV, @MaLoai_CC, N'Củng cố Bảng chữ cái', N'Luyện tập bảng chữ cái', 50),
('CC012', @MaMH_TV, @MaLoai_CC, N'Củng cố Vần âm', N'Luyện tập vần và âm', 50),
('CC013', @MaMH_TV, @MaLoai_CC, N'Củng cố Từ đơn', N'Luyện tập từ đơn', 50),
('CC014', @MaMH_TV, @MaLoai_CC, N'Củng cố Từ ghép', N'Luyện tập từ ghép', 50),
('CC015', @MaMH_TV, @MaLoai_CC, N'Củng cố Câu đơn', N'Luyện tập câu đơn', 50),
('CC016', @MaMH_TV, @MaLoai_CC, N'Củng cố Dấu câu', N'Luyện tập dấu câu', 50),
('CC017', @MaMH_TV, @MaLoai_CC, N'Củng cố Đồng âm', N'Luyện tập từ đồng âm', 50),
('CC018', @MaMH_TV, @MaLoai_CC, N'Củng cố Trái nghĩa', N'Luyện tập từ trái nghĩa', 50),
('CC019', @MaMH_TV, @MaLoai_CC, N'Củng cố Thành ngữ', N'Luyện tập thành ngữ', 50),
('CC020', @MaMH_TV, @MaLoai_CC, N'Củng cố Thơ', N'Luyện đọc hiểu thơ', 50);

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
-- =========================================================
-- CC001 – PHÉP CỘNG (CH001–CH010)
('CH001', N'2 + 3 = ?', N'Đây là phép cộng. Con hãy đếm số thứ nhất rồi đếm thêm số thứ hai để tìm kết quả.', 5),
('CH002', N'5 + 1 = ?', N'Đây là phép cộng. Con cộng thêm 1 vào số đã cho.', 5),
('CH003', N'4 + 4 = ?', N'Đây là phép cộng. Con có thể đếm thêm hoặc nhớ bảng cộng.', 5),
('CH004', N'6 + 3 = ?', N'Đây là phép cộng. Con hãy cộng hai số lại để biết tổng.', 5),
('CH005', N'7 + 1 = ?', N'Đây là phép cộng. Khi cộng thêm 1, kết quả tăng thêm 1 đơn vị.', 5),
('CH006', N'12 + 5 = ?', N'Đây là phép cộng. Con cộng hàng đơn vị trước, rồi đến hàng chục.', 5),
('CH007', N'8 + 6 = ?', N'Đây là phép cộng. Con có thể tách số cho dễ tính.', 5),
('CH008', N'13 + 4 = ?', N'Đây là phép cộng. Con hãy cộng thêm 4 vào 13.', 5),
('CH009', N'15 + 7 = ?', N'Đây là phép cộng. Con hãy cộng từng phần để tìm kết quả.', 5),
('CH010', N'9 + 8 = ?', N'Đây là phép cộng. Con có thể làm tròn rồi cộng tiếp.', 5),

-- =========================================================
-- CC002 – PHÉP TRỪ (CH011–CH020)
('CH011', N'6 - 3 = ?', N'Đây là phép trừ. Con bắt đầu từ số lớn rồi bớt đi số đã cho.', 5),
('CH012', N'10 - 2 = ?', N'Đây là phép trừ. Con lấy 10 bớt đi 2.', 5),
('CH013', N'8 - 1 = ?', N'Đây là phép trừ. Khi trừ đi 1, kết quả giảm 1 đơn vị.', 5),
('CH014', N'6 - 4 = ?', N'Đây là phép trừ. Con hãy đếm lùi để tìm kết quả.', 5),
('CH015', N'10 - 3 = ?', N'Đây là phép trừ. Con bớt đi 3 từ số ban đầu.', 5),
('CH016', N'14 - 6 = ?', N'Đây là phép trừ. Con trừ hàng đơn vị trước.', 5),
('CH017', N'11 - 5 = ?', N'Đây là phép trừ. Con có thể đếm lùi 5 bước.', 5),
('CH018', N'12 - 3 = ?', N'Đây là phép trừ. Con bớt đi 3 để tìm số còn lại.', 5),
('CH019', N'16 - 9 = ?', N'Đây là phép trừ. Con hãy trừ từng phần cho dễ tính.', 5),
('CH020', N'17 - 7 = ?', N'Đây là phép trừ. Con bớt đi 7 từ 17.', 5),

-- =========================================================
-- CC003 – PHÉP NHÂN (CH021–CH030)
('CH021', N'2 × 3 = ?', N'Đây là phép nhân. Phép nhân là cách cộng một số nhiều lần.', 5),
('CH022', N'4 × 2 = ?', N'Đây là phép nhân. Con có thể cộng 4 hai lần.', 5),
('CH023', N'3 × 5 = ?', N'Đây là phép nhân. Con cộng số 3 năm lần.', 5),
('CH024', N'6 × 2 = ?', N'Đây là phép nhân. Con hãy nhớ bảng nhân.', 5),
('CH025', N'4 × 4 = ?', N'Đây là phép nhân. Con có thể cộng 4 bốn lần.', 5),
('CH026', N'5 × 3 = ?', N'Đây là phép nhân. Phép nhân giúp tính nhanh hơn.', 5),
('CH027', N'6 × 4 = ?', N'Đây là phép nhân. Con hãy nhân hai số đã cho.', 5),
('CH028', N'7 × 2 = ?', N'Đây là phép nhân. Nhân với 2 nghĩa là gấp đôi.', 5),
('CH029', N'8 × 3 = ?', N'Đây là phép nhân. Con có thể cộng 8 ba lần.', 5),
('CH030', N'9 × 2 = ?', N'Đây là phép nhân. Nhân với 2 là lấy gấp đôi.', 5),

-- =========================================================
-- CC004 – PHÉP CHIA (CH031–CH040)
('CH031', N'6 ÷ 2 = ?', N'Đây là phép chia. Con chia đều 6 thành 2 phần.', 5),
('CH032', N'8 ÷ 4 = ?', N'Đây là phép chia. Con chia đều để tìm kết quả.', 5),
('CH033', N'10 ÷ 2 = ?', N'Đây là phép chia. Con chia 10 thành 2 phần.', 5),
('CH034', N'12 ÷ 3 = ?', N'Đây là phép chia. Con chia đều cho 3.', 5),
('CH035', N'14 ÷ 2 = ?', N'Đây là phép chia. Mỗi phần sẽ bằng nhau.', 5),
('CH036', N'16 ÷ 4 = ?', N'Đây là phép chia. Con hãy chia đều.', 5),
('CH037', N'18 ÷ 3 = ?', N'Đây là phép chia. Con chia đều 18 cho 3.', 5),
('CH038', N'20 ÷ 5 = ?', N'Đây là phép chia. Con chia số đã cho thành 5 phần.', 5),
('CH039', N'21 ÷ 3 = ?', N'Đây là phép chia. Con chia đều để tìm mỗi phần.', 5),
('CH040', N'24 ÷ 6 = ?', N'Đây là phép chia. Con hãy nhớ bảng chia.', 5),

-- =========================================================
-- CC005 – HÌNH HỌC (CH041–CH050)
('CH041', N'Hình vuông có mấy cạnh?', N'Con hãy nhớ hình vuông có các cạnh bằng nhau.', 5),
('CH042', N'Hình tròn có đặc điểm nào sau đây?', N'Con hãy nhớ hình tròn không có cạnh và góc.', 5),
('CH043', N'Hình tam giác có mấy cạnh?', N'Hình tam giác có ba cạnh.', 5),
('CH044', N'Hình chữ nhật có mấy cạnh?', N'Hình chữ nhật có bốn cạnh.', 5),
('CH045', N'Hình vuông có mấy góc?', N'Hình vuông có các góc vuông.', 5),
('CH046', N'Hình tam giác có mấy góc?', N'Con hãy đếm số góc của hình tam giác.', 5),
('CH047', N'Hình chữ nhật có mấy góc?', N'Hình chữ nhật có bốn góc vuông.', 5),
('CH048', N'Hình nào sau đây không có góc?', N'Con hãy nhớ đặc điểm của các hình đã học.', 5),
('CH049', N'Hình nào có bốn cạnh bằng nhau?', N'Con hãy chọn hình có các cạnh bằng nhau.', 5),
('CH050', N'Hình nào sau đây không có cạnh?', N'Con hãy quan sát đặc điểm các hình.', 5),

-- =========================================================
-- CC006 – ĐƠN VỊ ĐO (CH051–CH060)
('CH051', N'Đơn vị nào dùng để đo độ dài?', N'Con hãy nhớ các đơn vị đo độ dài.', 5),
('CH052', N'Đơn vị nào dùng để đo khối lượng?', N'Con hãy nhớ đơn vị đo khối lượng.', 5),
('CH053', N'1 mét bằng bao nhiêu xăng-ti-mét?', N'Con hãy nhớ mối quan hệ giữa các đơn vị.', 5),
('CH054', N'1 ki-lô-gam bằng bao nhiêu gam?', N'Con hãy nhớ cách đổi đơn vị.', 5),
('CH055', N'Độ dài cái bàn thường đo bằng đơn vị nào?', N'Con hãy chọn đơn vị đo phù hợp.', 5),
('CH056', N'Cân nặng quả táo thường đo bằng đơn vị nào?', N'Con hãy suy nghĩ đơn vị phù hợp.', 5),
('CH057', N'Độ dài con đường thường đo bằng đơn vị nào?', N'Con hãy chọn đơn vị đo lớn.', 5),
('CH058', N'Khối lượng túi gạo thường đo bằng đơn vị nào?', N'Con hãy nhớ đơn vị đo khối lượng.', 5),
('CH059', N'Đơn vị nào sau đây lớn hơn xăng-ti-mét?', N'Con hãy so sánh các đơn vị đo.', 5),
('CH060', N'Đơn vị nào sau đây nặng hơn gam?', N'Con hãy so sánh các đơn vị khối lượng.', 5),

-- =========================================================
-- CC007 – PHÂN SỐ (CH061–CH070)
('CH061', N'Phân số 1/2 biểu thị điều gì?', N'Phân số cho biết một phần của một vật.', 5),
('CH062', N'Phân số 1/3 biểu thị điều gì?', N'Vật được chia thành các phần bằng nhau.', 5),
('CH063', N'Trong phân số 1/4, số 4 cho biết điều gì?', N'Số ở dưới cho biết số phần bằng nhau.', 5),
('CH064', N'Phân số nào biểu thị một nửa?', N'Con hãy nhớ cách đọc phân số.', 5),
('CH065', N'So sánh 1/2 với 1, kết quả đúng là:', N'Phân số có tử nhỏ hơn mẫu thì nhỏ hơn 1.', 5),
('CH066', N'So sánh 1/3 và 1/2, kết quả đúng là:', N'Con hãy so sánh hai phân số.', 5),
('CH067', N'Một chiếc bánh chia 4 phần, ăn 1 phần là phân số nào?', N'Con hãy xác định phần đã ăn.', 5),
('CH068', N'Phân số nào sau đây nhỏ hơn 1?', N'Con hãy chọn phân số có tử nhỏ hơn mẫu.', 5),
('CH069', N'Phân số dùng để biểu thị điều gì?', N'Phân số biểu thị một phần của một vật.', 5),
('CH070', N'Phân số dùng trong trường hợp nào?', N'Phân số dùng khi vật được chia đều.', 5),

-- =========================================================
-- CC008 – THẬP PHÂN (CH071–CH080)
('CH071', N'Số 0.5 biểu thị điều gì?', N'Số thập phân biểu thị phần nhỏ hơn 1.', 5),
('CH072', N'Số nào sau đây lớn hơn 0.2?', N'Con hãy so sánh giá trị các số.', 5),
('CH073', N'Trong số 1.5, chữ số 5 chỉ điều gì?', N'Con hãy chú ý phần sau dấu phẩy.', 5),
('CH074', N'Số nào lớn hơn: 0.3 hay 0.7?', N'Con hãy so sánh phần thập phân.', 5),
('CH075', N'0.1 biểu thị bao nhiêu phần mười?', N'Con hãy nhớ ý nghĩa chữ số sau dấu phẩy.', 5),
('CH076', N'Số nào sau đây nhỏ hơn 1?', N'Số thập phân nhỏ hơn 1 có phần nguyên bằng 0.', 5),
('CH077', N'Số thập phân dùng để làm gì?', N'Số thập phân dùng để biểu thị phần nhỏ.', 5),
('CH078', N'Số 0.8 gần với số nào hơn?', N'Con hãy so sánh khoảng cách đến 0 và 1.', 5),
('CH079', N'Số nào sau đây lớn hơn 0.4?', N'Con hãy so sánh các số thập phân.', 5),
('CH080', N'Số thập phân có ký hiệu gì ở giữa?', N'Số thập phân có dấu phẩy.', 5),

-- =========================================================
-- CC009 – THỜI GIAN (CH081–CH090)
('CH081', N'1 giờ có bao nhiêu phút?', N'Con hãy nhớ 1 giờ bằng 60 phút.', 5),
('CH082', N'30 phút tương ứng với khoảng thời gian nào?', N'Con hãy đổi phút sang giờ.', 5),
('CH083', N'Kim giờ trên đồng hồ chỉ điều gì?', N'Con hãy quan sát kim giờ.', 5),
('CH084', N'Kim phút trên đồng hồ chỉ điều gì?', N'Con hãy quan sát kim phút.', 5),
('CH085', N'Đồng hồ chỉ 7 giờ nghĩa là gì?', N'Con hãy đọc giờ trên đồng hồ.', 5),
('CH086', N'Đồng hồ chỉ 7 giờ 30 phút nghĩa là gì?', N'Con hãy đọc cả giờ và phút.', 5),
('CH087', N'Một ngày có bao nhiêu giờ?', N'Con hãy nhớ số giờ trong một ngày.', 5),
('CH088', N'Sau 3 giờ nữa kể từ 2 giờ là mấy giờ?', N'Con hãy cộng thêm số giờ.', 5),
('CH089', N'15 phút tương ứng với khoảng thời gian nào sau đây?', N'Con hãy nhớ mối quan hệ giữa phút và giờ.', 5),
('CH090', N'Đọc giờ giúp con làm được việc gì?', N'Đọc giờ giúp con sắp xếp thời gian.', 5),

-- =========================================================
-- CC010 – TIỀN TỆ (CH091–CH100)
('CH091', N'10 000đ + 5 000đ = ?', N'Con hãy cộng các số tiền.', 5),
('CH092', N'20 000đ - 5 000đ = ?', N'Con hãy trừ số tiền đã cho.', 5),
('CH093', N'Mua bút 5 000đ và vở 10 000đ, cần bao nhiêu tiền?', N'Con hãy cộng các số tiền cần trả.', 5),
('CH094', N'Có 20 000đ, mua đồ hết 15 000đ, còn lại bao nhiêu?', N'Con hãy trừ số tiền đã dùng.', 5),
('CH095', N'2 tờ 10 000đ bằng bao nhiêu tiền?', N'Con hãy cộng các tờ tiền giống nhau.', 5),
('CH096', N'3 tờ 5 000đ bằng bao nhiêu tiền?', N'Con hãy nhân số tiền với số tờ.', 5),
('CH097', N'Mua 2 quyển vở, mỗi quyển 8 000đ, cần bao nhiêu tiền?', N'Con hãy cộng hoặc nhân để tính tổng tiền.', 5),
('CH098', N'Có 50 000đ, mua đồ hết 32 000đ, còn lại bao nhiêu?', N'Con hãy trừ số tiền đã chi.', 5),
('CH099', N'1 tờ 20 000đ đổi được mấy tờ 10 000đ?', N'Con hãy chia số tiền thành các phần bằng nhau.', 5),
('CH100', N'Tiền dùng để làm gì?', N'Tiền dùng để mua bán và trao đổi hàng hóa.', 5),

-- =========================================================
-- 2) CỦNG CỐ TIẾNG VIỆT: 100 câu (CH101–CH200)
-- =========================================================
-- CC011 – CỦNG CỐ BẢNG CHỮ CÁI (CH101–CH110)
('CH101', N'Chữ cái nào đứng sau chữ A?', N'Con hãy nhớ thứ tự các chữ cái trong bảng chữ cái.', 5),
('CH102', N'Chữ cái nào đứng trước chữ D?', N'Con hãy đọc thứ tự bảng chữ cái.', 5),
('CH103', N'Bảng chữ cái tiếng Việt có bao nhiêu chữ?', N'Con hãy nhớ số chữ cái đã học.', 5),
('CH104', N'Chữ cái nào đứng giữa chữ B và D?', N'Con hãy đọc thứ tự các chữ cái.', 5),
('CH105', N'Chữ cái nào đứng cuối bảng chữ cái?', N'Con hãy nhớ chữ cái cuối cùng.', 5),
('CH106', N'Nhóm chữ nào sau đây đều là chữ cái?', N'Con hãy phân biệt chữ cái với số và ký hiệu.', 5),
('CH107', N'Chữ cái nào dùng để viết từ “bàn”?', N'Con hãy đọc từ và xác định chữ cái.', 5),
('CH108', N'Chữ cái nào viết hoa trong tên riêng?', N'Con hãy nhớ cách viết tên riêng.', 5),
('CH109', N'Chữ cái nào có nét cong?', N'Con hãy quan sát hình dạng chữ cái.', 5),
('CH110', N'Chữ cái nào không có nét thẳng?', N'Con hãy quan sát hình dạng các chữ.', 5),

-- =========================================================
-- CC012 – CỦNG CỐ VẦN ÂM (CH111–CH120)
('CH111', N'Vần nào sau đây có âm “an”?', N'Con hãy đọc kỹ các vần.', 5),
('CH112', N'Vần nào sau đây có âm “ong”?', N'Con hãy phân biệt các vần đã học.', 5),
('CH113', N'Tiếng nào có vần “at”?', N'Con hãy tách tiếng thành âm và vần.', 5),
('CH114', N'Tiếng nào có vần “en”?', N'Con hãy đọc và nhận biết vần.', 5),
('CH115', N'Vần nào dùng để ghép thành tiếng “bàn”?', N'Con hãy tách tiếng thành âm và vần.', 5),
('CH116', N'Vần nào sau đây có âm cuối là “n”?', N'Con hãy chú ý âm cuối của vần.', 5),
('CH117', N'Vần nào có âm “oa”?', N'Con hãy đọc kỹ các vần.', 5),
('CH118', N'Tiếng nào có vần “oi”?', N'Con hãy xác định vần trong tiếng.', 5),
('CH119', N'Vần nào thường dùng trong từ “hoa”?', N'Con hãy nhớ cách ghép vần.', 5),
('CH120', N'Vần nào có âm chính là “ê”?', N'Con hãy nhận biết âm chính.', 5),

-- =========================================================
-- CC013 – CỦNG CỐ TỪ ĐƠN (CH121–CH130)
('CH121', N'Từ nào sau đây là từ đơn?', N'Từ đơn chỉ gồm một tiếng.', 5),
('CH122', N'Từ nào là từ đơn chỉ người?', N'Từ đơn có thể chỉ người, vật.', 5),
('CH123', N'Từ nào là từ đơn chỉ đồ vật?', N'Con hãy chọn từ chỉ một vật.', 5),
('CH124', N'Từ nào là từ đơn chỉ con vật?', N'Con hãy chọn từ gọi tên con vật.', 5),
('CH125', N'Từ nào là từ đơn chỉ sự vật?', N'Con hãy chọn từ chỉ sự vật.', 5),
('CH126', N'Từ nào sau đây chỉ gồm một tiếng?', N'Con hãy đếm số tiếng của từ.', 5),
('CH127', N'Từ nào không phải là từ ghép?', N'Từ đơn không do nhiều tiếng ghép lại.', 5),
('CH128', N'Từ nào là từ đơn dùng trong gia đình?', N'Con hãy chọn từ quen thuộc.', 5),
('CH129', N'Từ nào là từ đơn chỉ đồ dùng học tập?', N'Con hãy chọn từ gọi tên đồ vật.', 5),
('CH130', N'Từ nào là từ đơn chỉ thiên nhiên?', N'Con hãy chọn từ phù hợp.', 5),

-- =========================================================
-- CC014 – CỦNG CỐ TỪ GHÉP (CH131–CH140)
('CH131', N'Từ nào sau đây là từ ghép?', N'Từ ghép do hai tiếng có nghĩa ghép lại.', 5),
('CH132', N'Từ nào là từ ghép chỉ đồ vật?', N'Con hãy chọn từ gồm hai tiếng.', 5),
('CH133', N'Từ nào là từ ghép chỉ con người?', N'Con hãy chọn từ ghép phù hợp.', 5),
('CH134', N'Từ nào là từ ghép chỉ nơi chốn?', N'Con hãy nhận biết từ ghép.', 5),
('CH135', N'Từ nào gồm hai tiếng đều có nghĩa?', N'Đó là đặc điểm của từ ghép.', 5),
('CH136', N'Từ nào là từ ghép chỉ hoạt động?', N'Con hãy phân biệt từ ghép và từ đơn.', 5),
('CH137', N'Từ nào là từ ghép quen thuộc?', N'Con hãy chọn từ thường dùng.', 5),
('CH138', N'Từ nào không phải là từ ghép?', N'Con hãy phân biệt từ ghép.', 5),
('CH139', N'Từ nào là từ ghép chỉ gia đình?', N'Con hãy chọn từ đúng.', 5),
('CH140', N'Từ nào là từ ghép chỉ học tập?', N'Con hãy nhớ các từ ghép đã học.', 5),

-- =========================================================
-- CC015 – CỦNG CỐ CÂU ĐƠN (CH141–CH150)
('CH141', N'Câu nào sau đây là câu đơn?', N'Câu đơn chỉ có một ý chính.', 5),
('CH142', N'Câu nào có đủ chủ ngữ và vị ngữ?', N'Con hãy xác định các bộ phận câu.', 5),
('CH143', N'Câu nào dùng để kể?', N'Câu kể dùng để nói hoặc kể.', 5),
('CH144', N'Câu nào là câu đơn kể hoạt động?', N'Con hãy xác định hoạt động trong câu.', 5),
('CH145', N'Câu nào là câu đơn tả sự vật?', N'Con hãy chú ý từ ngữ miêu tả.', 5),
('CH146', N'Câu nào có một chủ ngữ?', N'Câu đơn thường có một chủ ngữ.', 5),
('CH147', N'Câu nào có một vị ngữ?', N'Câu đơn có một ý chính.', 5),
('CH148', N'Câu nào không phải là câu ghép?', N'Con hãy phân biệt câu đơn và câu ghép.', 5),
('CH149', N'Câu nào là câu đơn hoàn chỉnh?', N'Câu cần đủ ý.', 5),
('CH150', N'Câu nào phù hợp khi kể chuyện?', N'Con hãy chọn câu phù hợp.', 5),

-- =========================================================
-- CC016 – CỦNG CỐ DẤU CÂU (CH151–CH160)
('CH151', N'Câu nào sau đây cần dùng dấu chấm?', N'Dấu chấm dùng để kết thúc câu kể.', 5),
('CH152', N'Câu nào cần dùng dấu hỏi?', N'Dấu hỏi dùng cho câu hỏi.', 5),
('CH153', N'Câu nào cần dùng dấu chấm than?', N'Dấu chấm than dùng cho câu cảm.', 5),
('CH154', N'Dấu nào dùng để ngăn cách các ý?', N'Dấu phẩy dùng để ngăn cách.', 5),
('CH155', N'Câu nào dùng dấu phẩy đúng?', N'Con hãy chọn câu đúng dấu.', 5),
('CH156', N'Dấu nào dùng ở cuối câu kể?', N'Con hãy nhớ vị trí dấu câu.', 5),
('CH157', N'Dấu nào dùng cho câu hỏi?', N'Con hãy chọn dấu phù hợp.', 5),
('CH158', N'Dấu nào thể hiện cảm xúc mạnh?', N'Con hãy nhớ công dụng dấu câu.', 5),
('CH159', N'Câu nào đặt dấu câu đúng?', N'Con hãy kiểm tra dấu câu.', 5),
('CH160', N'Dấu câu nào thường dùng nhất?', N'Con hãy nhớ các dấu đã học.', 5),

-- =========================================================
-- CC017 – CỦNG CỐ ĐỒNG ÂM (CH161–CH170)
('CH161', N'Từ nào sau đây là từ đồng âm?', N'Từ đồng âm có cách đọc giống nhau.', 5),
('CH162', N'Cặp từ nào có âm giống nhau?', N'Con hãy chú ý cách đọc.', 5),
('CH163', N'Từ nào có nghĩa khác nhưng đọc giống?', N'Đó là đặc điểm của từ đồng âm.', 5),
('CH164', N'Từ nào sau đây là từ đồng âm khác nghĩa?', N'Con hãy phân biệt nghĩa của từ.', 5),
('CH165', N'Từ nào đọc giống nhưng nghĩa khác?', N'Con hãy nhớ khái niệm đồng âm.', 5),
('CH166', N'Cặp từ nào là đồng âm?', N'Con hãy so sánh cách đọc.', 5),
('CH167', N'Từ đồng âm thường gây điều gì?', N'Từ đồng âm dễ gây nhầm lẫn.', 5),
('CH168', N'Từ nào là từ đồng âm trong câu?', N'Con hãy đọc kỹ câu.', 5),
('CH169', N'Từ đồng âm khác nhau ở điểm nào?', N'Con hãy phân biệt nghĩa.', 5),
('CH170', N'Từ đồng âm cần hiểu gì để dùng đúng?', N'Con cần hiểu nghĩa của từ.', 5),

-- =========================================================
-- CC018 – CỦNG CỐ TRÁI NGHĨA (CH171–CH180)
('CH171', N'Từ nào có nghĩa trái với “cao”?', N'Từ trái nghĩa có nghĩa ngược nhau.', 5),
('CH172', N'Từ nào trái nghĩa với “nhanh”?', N'Con hãy tìm từ ngược nghĩa.', 5),
('CH173', N'Cặp từ nào là trái nghĩa?', N'Con hãy chọn cặp từ đối lập.', 5),
('CH174', N'Từ nào trái nghĩa với “vui”?', N'Con hãy tìm từ có nghĩa ngược.', 5),
('CH175', N'Từ nào trái nghĩa với “sạch”?', N'Con hãy nhớ các cặp trái nghĩa.', 5),
('CH176', N'Từ nào trái nghĩa với “to”?', N'Con hãy chọn từ phù hợp.', 5),
('CH177', N'Cặp từ nào có nghĩa trái ngược?', N'Con hãy chọn cặp đúng.', 5),
('CH178', N'Từ nào trái nghĩa với “sáng”?', N'Con hãy chọn từ đối lập.', 5),
('CH179', N'Từ nào trái nghĩa với “nóng”?', N'Con hãy tìm từ ngược nghĩa.', 5),
('CH180', N'Từ trái nghĩa dùng để làm gì?', N'Từ trái nghĩa giúp diễn đạt rõ hơn.', 5),

-- =========================================================
-- CC019 – CỦNG CỐ THÀNH NGỮ (CH181–CH190)
('CH181', N'Thành ngữ nào nói về công ơn cha mẹ?', N'Thành ngữ mang ý nghĩa sâu sắc.', 5),
('CH182', N'Thành ngữ nào khuyên con người chăm chỉ?', N'Con hãy chọn thành ngữ phù hợp.', 5),
('CH183', N'Thành ngữ nào nói về tình bạn?', N'Con hãy nhớ các thành ngữ đã học.', 5),
('CH184', N'Thành ngữ nào dùng để khen người tốt?', N'Con hãy chọn thành ngữ đúng.', 5),
('CH185', N'Thành ngữ nào nói về học tập?', N'Con hãy chọn thành ngữ quen thuộc.', 5),
('CH186', N'Thành ngữ nào nói về đoàn kết?', N'Con hãy nhớ ý nghĩa thành ngữ.', 5),
('CH187', N'Thành ngữ nào dùng để khuyên nhủ?', N'Con hãy chọn thành ngữ phù hợp.', 5),
('CH188', N'Thành ngữ nào nói về lòng biết ơn?', N'Con hãy hiểu nghĩa thành ngữ.', 5),
('CH189', N'Thành ngữ nào dùng trong giao tiếp?', N'Con hãy chọn thành ngữ đúng.', 5),
('CH190', N'Thành ngữ thường dùng để làm gì?', N'Thành ngữ giúp câu nói hay hơn.', 5),

-- =========================================================
-- CC020 – CỦNG CỐ THƠ (CH191–CH200)
('CH191', N'Bài thơ thường được viết theo hình thức nào?', N'Bài thơ có vần điệu.', 5),
('CH192', N'Thơ thường dùng để làm gì?', N'Thơ dùng để bộc lộ cảm xúc.', 5),
('CH193', N'Trong thơ, từ ngữ thường có đặc điểm gì?', N'Từ ngữ trong thơ giàu hình ảnh.', 5),
('CH194', N'Thơ thường có yếu tố nào?', N'Thơ thường có vần.', 5),
('CH195', N'Bài thơ giúp con cảm nhận điều gì?', N'Thơ giúp cảm nhận vẻ đẹp.', 5),
('CH196', N'Trong thơ, câu thơ dùng để làm gì?', N'Câu thơ thể hiện ý thơ.', 5),
('CH197', N'Thơ thường có nhịp điệu như thế nào?', N'Nhịp điệu giúp thơ dễ nhớ.', 5),
('CH198', N'Khi đọc thơ, con cần chú ý điều gì?', N'Con cần đọc đúng nhịp.', 5),
('CH199', N'Thơ giúp con học được điều gì?', N'Thơ giúp giáo dục cảm xúc.', 5),
('CH200', N'Đọc thơ giúp con phát triển điều gì?', N'Đọc thơ giúp phát triển ngôn ngữ.', 5),

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
('CHG040', N'Tính: 30 - 6 × 3 = ?', N'Nhân trước, rồi trừ.', 5),

('CHG041', N'Tính: 4 + 5 = ?', N'Bé hãy dùng quy tắc phép cộng để làm bài này nhé.', 5),
('CHG042', N'Tính: 9 - 3 = ?', N'Bé hãy dùng quy tắc phép trừ để làm bài này nhé.', 5),
('CHG043', N'Tính: 6 + 2 = ?', N'Bé hãy dùng quy tắc phép cộng để làm bài này nhé.', 5),
('CHG044', N'Tính: 8 - 4 = ?', N'Bé hãy dùng quy tắc phép trừ để làm bài này nhé.', 5),
('CHG045', N'Tính: 3 × 2 = ?', N'Bé hãy dùng quy tắc phép nhân để làm bài này nhé.', 5),
('CHG046', N'Tính: 12 ÷ 3 = ?', N'Bé hãy dùng quy tắc phép chia để làm bài này nhé.', 5),
('CHG047', N'Tính: 7 + 6 = ?', N'Bé hãy dùng quy tắc phép cộng để làm bài này nhé.', 5),
('CHG048', N'Tính: 15 - 7 = ?', N'Bé hãy dùng quy tắc phép trừ để làm bài này nhé.', 5),
('CHG049', N'Tính: 4 × 3 = ?', N'Bé hãy dùng quy tắc phép nhân để làm bài này nhé.', 5),
('CHG050', N'Tính: 16 ÷ 4 = ?', N'Bé hãy dùng quy tắc phép chia để làm bài này nhé.', 5),
('CHG051', N'Tính: 5 + 7 = ?', N'Bé hãy dùng quy tắc phép cộng để làm bài này nhé.', 5),
('CHG052', N'Tính: 9 - 5 = ?', N'Bé hãy dùng quy tắc phép trừ để làm bài này nhé.', 5),
('CHG053', N'Tính: 2 × 6 = ?', N'Bé hãy dùng quy tắc phép nhân để làm bài này nhé.', 5),
('CHG054', N'Tính: 14 ÷ 2 = ?', N'Bé hãy dùng quy tắc phép chia để làm bài này nhé.', 5),
('CHG055', N'Tính: 8 + 3 = ?', N'Bé hãy dùng quy tắc phép cộng để làm bài này nhé.', 5),
('CHG056', N'Tính: 10 - 6 = ?', N'Bé hãy dùng quy tắc phép trừ để làm bài này nhé.', 5),
('CHG057', N'Tính: 3 × 4 = ?', N'Bé hãy dùng quy tắc phép nhân để làm bài này nhé.', 5),
('CHG058', N'Tính: 18 ÷ 6 = ?', N'Bé hãy dùng quy tắc phép chia để làm bài này nhé.', 5),
('CHG059', N'Tính: 6 + 7 = ?', N'Bé hãy dùng quy tắc phép cộng để làm bài này nhé.', 5),
('CHG060', N'Tính: 11 - 3 = ?', N'Bé hãy dùng quy tắc phép trừ để làm bài này nhé.', 5);

GO

---------------------------------------------
-- TẠO DAPAN CHO CÁC CÂU Toan Cung Co
---------------------------------------------
USE UngDungHocTapChoTre;
GO

INSERT INTO DapAn (MaDapAn, MaCauHoi, NoiDungDapAn, LaDapAnDung) VALUES
	-- CH001: 2 + 3 = ?
	('DA1501','CH001',N'7',0),
	('DA1502','CH001',N'5',1),
	('DA1503','CH001',N'4',0),
	('DA1504','CH001',N'6',0),
	-- CH002: 5 + 1 = ?
	('DA1505','CH002',N'6',1),
	('DA1506','CH002',N'5',0),
	('DA1507','CH002',N'7',0),
	('DA1508','CH002',N'4',0),
	-- CH003: 4 + 4 = ?
	('DA1509','CH003',N'7',0),
	('DA1510','CH003',N'9',0),
	('DA1511','CH003',N'8',1),
	('DA1512','CH003',N'6',0),
	-- CH004: 6 + 3 = ?
	('DA1513','CH004',N'9',1),
	('DA1514','CH004',N'8',0),
	('DA1515','CH004',N'10',0),
	('DA1516','CH004',N'7',0),
	-- CH005: 7 + 1 = ?
	('DA1517','CH005',N'8',1),
	('DA1518','CH005',N'6',0),
	('DA1519','CH005',N'9',0),
	('DA1520','CH005',N'7',0),
	-- CH006: 12 + 5 = ?
	('DA1521','CH006',N'16',0),
	('DA1522','CH006',N'18',0),
	('DA1523','CH006',N'17',1),
	('DA1524','CH006',N'15',0),
	-- CH007: 8 + 6 = ?
	('DA1525','CH007',N'14',1),
	('DA1526','CH007',N'13',0),
	('DA1527','CH007',N'15',0),
	('DA1528','CH007',N'12',0),
	-- CH008: 13 + 4 = ?
	('DA1529','CH008',N'18',0),
	('DA1530','CH008',N'16',0),
	('DA1531','CH008',N'17',1),
	('DA1532','CH008',N'15',0),
	-- CH009: 15 + 7 = ?
	('DA1533','CH009',N'22',1),
	('DA1534','CH009',N'21',0),
	('DA1535','CH009',N'23',0),
	('DA1536','CH009',N'20',0),
	-- CH010: 9 + 8 = ?
	('DA1537','CH010',N'17',1),
	('DA1538','CH010',N'16',0),
	('DA1539','CH010',N'18',0),
	('DA1540','CH010',N'15',0),
	-- CH011: 6 - 3 = ?
	('DA1541','CH011',N'2',0),
	('DA1542','CH011',N'3',1),
	('DA1543','CH011',N'4',0),
	('DA1544','CH011',N'1',0),
	-- CH012: 10 - 2 = ?
	('DA1545','CH012',N'8',1),
	('DA1546','CH012',N'9',0),
	('DA1547','CH012',N'7',0),
	('DA1548','CH012',N'6',0),
	-- CH013: 8 - 1 = ?
	('DA1549','CH013',N'6',0),
	('DA1550','CH013',N'7',1),
	('DA1551','CH013',N'8',0),
	('DA1552','CH013',N'5',0),
	-- CH014: 6 - 4 = ?
	('DA1553','CH014',N'3',0),
	('DA1554','CH014',N'1',0),
	('DA1555','CH014',N'2',1),
	('DA1556','CH014',N'4',0),
	-- CH015: 10 - 3 = ?
	('DA1557','CH015',N'7',1),
	('DA1558','CH015',N'8',0),
	('DA1559','CH015',N'6',0),
	('DA1560','CH015',N'5',0),
	-- CH016: 14 - 6 = ?
	('DA1561','CH016',N'7',0),
	('DA1562','CH016',N'9',0),
	('DA1563','CH016',N'8',1),
	('DA1564','CH016',N'6',0),
	-- CH017: 11 - 5 = ?
	('DA1565','CH017',N'6',1),
	('DA1566','CH017',N'7',0),
	('DA1567','CH017',N'5',0),
	('DA1568','CH017',N'4',0),
	-- CH018: 12 - 3 = ?
	('DA1569','CH018',N'8',0),
	('DA1570','CH018',N'10',0),
	('DA1571','CH018',N'9',1),
	('DA1572','CH018',N'7',0),
	-- CH019: 16 - 9 = ?
	('DA1573','CH019',N'6',0),
	('DA1574','CH019',N'7',1),
	('DA1575','CH019',N'8',0),
	('DA1576','CH019',N'5',0),
	-- CH020: 17 - 7 = ?
	('DA1577','CH020',N'10',1),
	('DA1578','CH020',N'11',0),
	('DA1579','CH020',N'9',0),
	('DA1580','CH020',N'8',0),
	-- CH021: 2 × 3 = ?
	('DA1581','CH021',N'5',0),
	('DA1582','CH021',N'6',1),
	('DA1583','CH021',N'7',0),
	('DA1584','CH021',N'4',0),
	-- CH022: 4 × 2 = ?
	('DA1585','CH022',N'7',0),
	('DA1586','CH022',N'9',0),
	('DA1587','CH022',N'8',1),
	('DA1588','CH022',N'6',0),
	-- CH023: 3 × 5 = ?
	('DA1589','CH023',N'15',1),
	('DA1590','CH023',N'12',0),
	('DA1591','CH023',N'18',0),
	('DA1592','CH023',N'10',0),
	-- CH024: 6 × 2 = ?
	('DA1593','CH024',N'12',1),
	('DA1594','CH024',N'11',0),
	('DA1595','CH024',N'13',0),
	('DA1596','CH024',N'10',0),
	-- CH025: 4 × 4 = ?
	('DA1597','CH025',N'15',0),
	('DA1598','CH025',N'18',0),
	('DA1599','CH025',N'16',1),
	('DA1600','CH025',N'14',0),

	('DA1601','CH026',N'15',1),
	('DA1602','CH026',N'14',0),
	('DA1603','CH026',N'16',0),
	('DA1604','CH026',N'12',0),
	-- CH027: 6 × 4 = ?
	('DA1605','CH027',N'22',0),
	('DA1606','CH027',N'24',1),
	('DA1607','CH027',N'26',0),
	('DA1608','CH027',N'20',0),
	-- CH028: 7 × 2 = ?
	('DA1609','CH028',N'13',0),
	('DA1610','CH028',N'15',0),
	('DA1611','CH028',N'14',1),
	('DA1612','CH028',N'12',0),
	-- CH029: 8 × 3 = ?
	('DA1613','CH029',N'24',1),
	('DA1614','CH029',N'22',0),
	('DA1615','CH029',N'26',0),
	('DA1616','CH029',N'20',0),
	-- CH030: 9 × 2 = ?
	('DA1617','CH030',N'18',1),
	('DA1618','CH030',N'17',0),
	('DA1619','CH030',N'19',0),
	('DA1620','CH030',N'16',0),
	-- CH031: 6 ÷ 2 = ?
	('DA1621','CH031',N'2',0),
	('DA1622','CH031',N'4',0),
	('DA1623','CH031',N'3',1),
	('DA1624','CH031',N'1',0),
	-- CH032: 8 ÷ 4 = ?
	('DA1625','CH032',N'3',0),
	('DA1626','CH032',N'2',1),
	('DA1627','CH032',N'4',0),
	('DA1628','CH032',N'1',0),
	-- CH033: 10 ÷ 2 = ?
	('DA1629','CH033',N'4',0),
	('DA1630','CH033',N'6',0),
	('DA1631','CH033',N'5',1),
	('DA1632','CH033',N'3',0),
	-- CH034: 12 ÷ 3 = ?
	('DA1633','CH034',N'4',1),
	('DA1634','CH034',N'5',0),
	('DA1635','CH034',N'3',0),
	('DA1636','CH034',N'2',0),
	-- CH035: 14 ÷ 2 = ?
	('DA1637','CH035',N'6',0),
	('DA1638','CH035',N'8',0),
	('DA1639','CH035',N'7',1),
	('DA1640','CH035',N'5',0),
	-- CH036: 16 ÷ 4 = ?
	('DA1641','CH036',N'3',0),
	('DA1642','CH036',N'5',0),
	('DA1643','CH036',N'4',1),
	('DA1644','CH036',N'2',0),
	-- CH037: 18 ÷ 3 = ?
	('DA1645','CH037',N'5',0),
	('DA1646','CH037',N'7',0),
	('DA1647','CH037',N'6',1),
	('DA1648','CH037',N'4',0),
	-- CH038: 20 ÷ 5 = ?
	('DA1649','CH038',N'3',0),
	('DA1650','CH038',N'5',0),
	('DA1651','CH038',N'4',1),
	('DA1652','CH038',N'2',0),
	-- CH039: 21 ÷ 3 = ?
	('DA1653','CH039',N'6',0),
	('DA1654','CH039',N'8',0),
	('DA1655','CH039',N'7',1),
	('DA1656','CH039',N'5',0),
	-- CH040: 24 ÷ 6 = ?
	('DA1657','CH040',N'3',0),
	('DA1658','CH040',N'5',0),
	('DA1659','CH040',N'4',1),
	('DA1660','CH040',N'2',0),
	-- CH041: Hình vuông có mấy cạnh?
	('DA1661','CH041',N'3 cạnh',0),
	('DA1662','CH041',N'4 cạnh',1),
	('DA1663','CH041',N'5 cạnh',0),
	('DA1664','CH041',N'6 cạnh',0),
	-- CH042: Hình tròn có đặc điểm nào sau đây?
	('DA1665','CH042',N'Có 4 cạnh',0),
	('DA1666','CH042',N'Không có cạnh và góc',1),
	('DA1667','CH042',N'Có 3 góc',0),
	('DA1668','CH042',N'Có 4 góc',0),
	-- CH043: Hình tam giác có mấy cạnh?
	('DA1669','CH043',N'2 cạnh',0),
	('DA1670','CH043',N'3 cạnh',1),
	('DA1671','CH043',N'4 cạnh',0),
	('DA1672','CH043',N'5 cạnh',0),
	-- CH044: Hình chữ nhật có mấy cạnh?
	('DA1673','CH044',N'3 cạnh',0),
	('DA1674','CH044',N'4 cạnh',1),
	('DA1675','CH044',N'5 cạnh',0),
	('DA1676','CH044',N'6 cạnh',0),
	-- CH045: Hình vuông có mấy góc?
	('DA1677','CH045',N'2 góc',0),
	('DA1678','CH045',N'3 góc',0),
	('DA1679','CH045',N'4 góc',1),
	('DA1680','CH045',N'5 góc',0),
	-- CH046: Hình tam giác có mấy góc?
	('DA1681','CH046',N'2 góc',0),
	('DA1682','CH046',N'3 góc',1),
	('DA1683','CH046',N'4 góc',0),
	('DA1684','CH046',N'5 góc',0),
	-- CH047: Hình chữ nhật có mấy góc?
	('DA1685','CH047',N'3 góc',0),
	('DA1686','CH047',N'4 góc',1),
	('DA1687','CH047',N'5 góc',0),
	('DA1688','CH047',N'6 góc',0),
	-- CH048: Hình nào sau đây không có góc?
	('DA1689','CH048',N'Hình vuông',0),
	('DA1690','CH048',N'Hình tam giác',0),
	('DA1691','CH048',N'Hình tròn',1),
	('DA1692','CH048',N'Hình chữ nhật',0),
	-- CH049: Hình nào có bốn cạnh bằng nhau?
	('DA1693','CH049',N'Hình tam giác',0),
	('DA1694','CH049',N'Hình chữ nhật',0),
	('DA1695','CH049',N'Hình vuông',1),
	('DA1696','CH049',N'Hình tròn',0),
	-- CH050: Hình nào sau đây không có cạnh?
	('DA1697','CH050',N'Hình vuông',0),
	('DA1698','CH050',N'Hình chữ nhật',0),
	('DA1699','CH050',N'Hình tròn',1),
	('DA1700','CH050',N'Hình tam giác',0),

	-- CH051: Đơn vị nào dùng để đo độ dài?
	('DA1701','CH051',N'Ki-lô-gam',0),
	('DA1702','CH051',N'Mét',1),
	('DA1703','CH051',N'Lít',0),
	('DA1704','CH051',N'Giây',0),
	-- CH052: Đơn vị nào dùng để đo khối lượng?
	('DA1705','CH052',N'Mét',0),
	('DA1706','CH052',N'Ki-lô-gam',1),
	('DA1707','CH052',N'Lít',0),
	('DA1708','CH052',N'Giây',0),
	-- CH053: 1 mét bằng bao nhiêu xăng-ti-mét?
	('DA1709','CH053',N'50 cm',0),
	('DA1710','CH053',N'100 cm',1),
	('DA1711','CH053',N'10 cm',0),
	('DA1712','CH053',N'1000 cm',0),
	-- CH054: 1 ki-lô-gam bằng bao nhiêu gam?
	('DA1713','CH054',N'100 gam',0),
	('DA1714','CH054',N'1000 gam',1),
	('DA1715','CH054',N'10 gam',0),
	('DA1716','CH054',N'500 gam',0),
	-- CH055: Độ dài cái bàn thường đo bằng đơn vị nào?
	('DA1717','CH055',N'Xăng-ti-mét',0),
	('DA1718','CH055',N'Mét',1),
	('DA1719','CH055',N'Ki-lô-mét',0),
	('DA1720','CH055',N'Mili-mét',0),
	-- CH056: Cân nặng quả táo thường đo bằng đơn vị nào?
	('DA1721','CH056',N'Ki-lô-gam',0),
	('DA1722','CH056',N'Gam',1),
	('DA1723','CH056',N'Tấn',0),
	('DA1724','CH056',N'Miligramm',0),
	-- CH057: Độ dài con đường thường đo bằng đơn vị nào?
	('DA1725','CH057',N'Mét',0),
	('DA1726','CH057',N'Ki-lô-mét',1),
	('DA1727','CH057',N'Xăng-ti-mét',0),
	('DA1728','CH057',N'Miligramm',0),
	-- CH058: Khối lượng túi gạo thường đo bằng đơn vị nào?
	('DA1729','CH058',N'Gam',0),
	('DA1730','CH058',N'Ki-lô-gam',1),
	('DA1731','CH058',N'Miligramm',0),
	('DA1732','CH058',N'Lít',0),
	-- CH059: Đơn vị nào sau đây lớn hơn xăng-ti-mét?
	('DA1733','CH059',N'Miligramm',0),
	('DA1734','CH059',N'Mét',1),
	('DA1735','CH059',N'Mili-mét',0),
	('DA1736','CH059',N'Không có',0),
	-- CH060: Đơn vị nào sau đây nặng hơn gam?
	('DA1737','CH060',N'Miligramm',0),
	('DA1738','CH060',N'Ki-lô-gam',1),
	('DA1739','CH060',N'Không có đơn vị',0),
	('DA1740','CH060',N'Mêtric',0),
	-- CH061: Phân số 1/2 biểu thị điều gì?
	('DA1741','CH061',N'Một phần ba',0),
	('DA1742','CH061',N'Một nửa',1),
	('DA1743','CH061',N'Một phần tư',0),
	('DA1744','CH061',N'Toàn bộ',0),
	-- CH062: Phân số 1/3 biểu thị điều gì?
	('DA1745','CH062',N'Một nửa',0),
	('DA1746','CH062',N'Một phần ba',1),
	('DA1747','CH062',N'Một phần tư',0),
	('DA1748','CH062',N'Toàn bộ',0),
	-- CH063: Trong phân số 1/4, số 4 cho biết điều gì?
	('DA1749','CH063',N'Số phần đã lấy',0),
	('DA1750','CH063',N'Số phần bằng nhau',1),
	('DA1751','CH063',N'Tổng các phần',0),
	('DA1752','CH063',N'Khoảng cách',0),
	-- CH064: Phân số nào biểu thị một nửa?
	('DA1753','CH064',N'1/3',0),
	('DA1754','CH064',N'1/2',1),
	('DA1755','CH064',N'1/4',0),
	('DA1756','CH064',N'1/5',0),
	-- CH065: So sánh 1/2 với 1, kết quả đúng là:
	('DA1757','CH065',N'1/2 > 1',0),
	('DA1758','CH065',N'1/2 = 1',0),
	('DA1759','CH065',N'1/2 < 1',1),
	('DA1760','CH065',N'Không so sánh được',0),
	-- CH066: So sánh 1/3 và 1/2, kết quả đúng là:
	('DA1761','CH066',N'1/3 > 1/2',0),
	('DA1762','CH066',N'1/3 = 1/2',0),
	('DA1763','CH066',N'1/3 < 1/2',1),
	('DA1764','CH066',N'Không so sánh được',0),
	-- CH067: Một chiếc bánh chia 4 phần, ăn 1 phần là phân số nào?
	('DA1765','CH067',N'1/2',0),
	('DA1766','CH067',N'1/3',0),
	('DA1767','CH067',N'1/4',1),
	('DA1768','CH067',N'1/5',0),
	-- CH068: Phân số nào sau đây nhỏ hơn 1?
	('DA1769','CH068',N'3/2',0),
	('DA1770','CH068',N'5/4',0),
	('DA1771','CH068',N'2/3',1),
	('DA1772','CH068',N'5/3',0),
	-- CH069: Phân số dùng để biểu thị điều gì?
	('DA1773','CH069',N'Toàn bộ',0),
	('DA1774','CH069',N'Một phần của một vật',1),
	('DA1775','CH069',N'Giá tiền',0),
	('DA1776','CH069',N'Thời gian',0),
	-- CH070: Phân số dùng trong trường hợp nào?
	('DA1777','CH070',N'Vật không chia',0),
	('DA1778','CH070',N'Vật được chia đều',1),
	('DA1779','CH070',N'Vật không thể chia',0),
	('DA1780','CH070',N'Không có trường hợp',0),
	-- CH071: Số 0.5 biểu thị điều gì?
	('DA1781','CH071',N'Năm',0),
	('DA1782','CH071',N'Năm phần mười',1),
	('DA1783','CH071',N'Năm trăm',0),
	('DA1784','CH071',N'Năm triệu',0),
	-- CH072: Số nào sau đây lớn hơn 0.2?
	('DA1785','CH072',N'0.1',0),
	('DA1786','CH072',N'0.15',0),
	('DA1787','CH072',N'0.3',1),
	('DA1788','CH072',N'0.05',0),
	-- CH073: Trong số 1.5, chữ số 5 chỉ điều gì?
	('DA1789','CH073',N'Hàng chục',0),
	('DA1790','CH073',N'Hàng phần mười',1),
	('DA1791','CH073',N'Hàng trăm',0),
	('DA1792','CH073',N'Hàng đơn vị',0),
	-- CH074: Số nào lớn hơn: 0.3 hay 0.7?
	('DA1793','CH074',N'0.3',0),
	('DA1794','CH074',N'Bằng nhau',0),
	('DA1795','CH074',N'0.7',1),
	('DA1796','CH074',N'Không so sánh được',0),
	-- CH075: 0.1 biểu thị bao nhiêu phần mười?
	('DA1797','CH075',N'Hai phần mười',0),
	('DA1798','CH075',N'Một phần mười',1),
	('DA1799','CH075',N'Mười phần mười',0),
	('DA1800','CH075',N'Năm phần mười',0),

	-- CH076: Số nào sau đây nhỏ hơn 1?
	('DA1801','CH076',N'1.5',0),
	('DA1802','CH076',N'1.1',0),
	('DA1803','CH076',N'0.8',1),
	('DA1804','CH076',N'1.8',0),
	-- CH077: Số thập phân dùng để làm gì?
	('DA1805','CH077',N'Biểu thị phần lớn',0),
	('DA1806','CH077',N'Biểu thị phần nhỏ',1),
	('DA1807','CH077',N'Biểu thị phần nguyên',0),
	('DA1808','CH077',N'Biểu thị tổng',0),
	-- CH078: Số 0.8 gần với số nào hơn?
	('DA1809','CH078',N'0',0),
	('DA1810','CH078',N'0.5',0),
	('DA1811','CH078',N'1',1),
	('DA1812','CH078',N'2',0),
	-- CH079: Số nào sau đây lớn hơn 0.4?
	('DA1813','CH079',N'0.3',0),
	('DA1814','CH079',N'0.2',0),
	('DA1815','CH079',N'0.6',1),
	('DA1816','CH079',N'0.1',0),
	-- CH080: Số thập phân có ký hiệu gì ở giữa?
	('DA1817','CH080',N'Dấu cộng',0),
	('DA1818','CH080',N'Dấu phẩy',1),
	('DA1819','CH080',N'Dấu trừ',0),
	('DA1820','CH080',N'Dấu nhân',0),
	-- CH081: 1 giờ có bao nhiêu phút?
	('DA1821','CH081',N'30 phút',0),
	('DA1822','CH081',N'60 phút',1),
	('DA1823','CH081',N'120 phút',0),
	('DA1824','CH081',N'45 phút',0),
	-- CH082: 30 phút tương ứng với khoảng thời gian nào?
	('DA1825','CH082',N'1 giờ',0),
	('DA1826','CH082',N'Nửa giờ',1),
	('DA1827','CH082',N'Một phần ba giờ',0),
	('DA1828','CH082',N'Một phần tư giờ',0),
	-- CH083: Kim giờ trên đồng hồ chỉ điều gì?
	('DA1829','CH083',N'Phút',0),
	('DA1830','CH083',N'Giờ',1),
	('DA1831','CH083',N'Giây',0),
	('DA1832','CH083',N'Ngày',0),
	-- CH084: Kim phút trên đồng hồ chỉ điều gì?
	('DA1833','CH084',N'Giờ',0),
	('DA1834','CH084',N'Phút',1),
	('DA1835','CH084',N'Giây',0),
	('DA1836','CH084',N'Ngày',0),
	-- CH085: Đồng hồ chỉ 7 giờ nghĩa là gì?
	('DA1837','CH085',N'7 phút',0),
	('DA1838','CH085',N'Kim giờ chỉ 7',1),
	('DA1839','CH085',N'7 giây',0),
	('DA1840','CH085',N'7 ngày',0),
	-- CH086: Đồng hồ chỉ 7 giờ 30 phút nghĩa là gì?
	('DA1841','CH086',N'7 giờ',0),
	('DA1842','CH086',N'7 giờ và 30 phút',1),
	('DA1843','CH086',N'7 phút',0),
	('DA1844','CH086',N'30 giờ',0),
	-- CH087: Một ngày có bao nhiêu giờ?
	('DA1845','CH087',N'12 giờ',0),
	('DA1846','CH087',N'24 giờ',1),
	('DA1847','CH087',N'30 giờ',0),
	('DA1848','CH087',N'60 giờ',0),
	-- CH088: Sau 3 giờ nữa kể từ 2 giờ là mấy giờ?
	('DA1849','CH088',N'4 giờ',0),
	('DA1850','CH088',N'5 giờ',1),
	('DA1851','CH088',N'6 giờ',0),
	('DA1852','CH088',N'3 giờ',0),
	-- CH089: 15 phút tương ứng với khoảng thời gian nào sau đây?
	('DA1853','CH089',N'Một phần ba giờ',1),
	('DA1854','CH089',N'Nửa giờ',0),
	('DA1855','CH089',N'Một phần năm giờ',0),
	('DA1856','CH089',N'Một giờ',0),
	-- CH090: Đọc giờ giúp con làm được việc gì?
	('DA1857','CH090',N'Sắp xếp thời gian',1),
	('DA1858','CH090',N'Tính toán số học',0),
	('DA1859','CH090',N'Vẽ hình',0),
	('DA1860','CH090',N'Chữa bài',0),
	-- CH091: 10 000đ + 5 000đ = ?
	('DA1861','CH091',N'12 000đ',0),
	('DA1862','CH091',N'15 000đ',1),
	('DA1863','CH091',N'14 000đ',0),
	('DA1864','CH091',N'16 000đ',0),
	-- CH092: 20 000đ - 5 000đ = ?
	('DA1865','CH092',N'15 000đ',1),
	('DA1866','CH092',N'16 000đ',0),
	('DA1867','CH092',N'14 000đ',0),
	('DA1868','CH092',N'25 000đ',0),
	-- CH093: Mua bút 5 000đ và vở 10 000đ, cần bao nhiêu tiền?
	('DA1869','CH093',N'12 000đ',0),
	('DA1870','CH093',N'15 000đ',1),
	('DA1871','CH093',N'14 000đ',0),
	('DA1872','CH093',N'16 000đ',0),
	-- CH094: Có 20 000đ, mua đồ hết 15 000đ, còn lại bao nhiêu?
	('DA1873','CH094',N'3 000đ',0),
	('DA1874','CH094',N'5 000đ',1),
	('DA1875','CH094',N'4 000đ',0),
	('DA1876','CH094',N'6 000đ',0),
	-- CH095: 2 tờ 10 000đ bằng bao nhiêu tiền?
	('DA1877','CH095',N'15 000đ',0),
	('DA1878','CH095',N'20 000đ',1),
	('DA1879','CH095',N'25 000đ',0),
	('DA1880','CH095',N'30 000đ',0),
	-- CH096: 3 tờ 5 000đ bằng bao nhiêu tiền?
	('DA1881','CH096',N'10 000đ',0),
	('DA1882','CH096',N'15 000đ',1),
	('DA1883','CH096',N'20 000đ',0),
	('DA1884','CH096',N'12 000đ',0),
	-- CH097: Mua 2 quyển vở, mỗi quyển 8 000đ, cần bao nhiêu tiền?
	('DA1885','CH097',N'12 000đ',0),
	('DA1886','CH097',N'16 000đ',1),
	('DA1887','CH097',N'18 000đ',0),
	('DA1888','CH097',N'14 000đ',0),
	-- CH098: Có 50 000đ, mua đồ hết 32 000đ, còn lại bao nhiêu?
	('DA1889','CH098',N'16 000đ',0),
	('DA1890','CH098',N'18 000đ',1),
	('DA1891','CH098',N'20 000đ',0),
	('DA1892','CH098',N'15 000đ',0),
	-- CH099: 1 tờ 20 000đ đổi được mấy tờ 10 000đ?
	('DA1893','CH099',N'3 tờ',0),
	('DA1894','CH099',N'2 tờ',1),
	('DA1895','CH099',N'4 tờ',0),
	('DA1896','CH099',N'1 tờ',0),
	-- CH100: Tiền dùng để làm gì?
	('DA1897','CH100',N'Chỉ để tiết kiệm',0),
	('DA1898','CH100',N'Mua bán và trao đổi hàng hóa',1),
	('DA1899','CH100',N'Để đo lường',0),
	('DA1900','CH100',N'Để xây dựng nhà',0);
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
    ('CHG040', 12), -- 30-6×3

	('CHG041',  9), -- 4 + 5
	('CHG042',  6), -- 9 - 3
	('CHG043',  8), -- 6 + 2
	('CHG044',  4), -- 8 - 4
	('CHG045',  6), -- 3 × 2
	('CHG046',  4), -- 12 ÷ 3
	('CHG047', 13), -- 7 + 6
	('CHG048',  8), -- 15 - 7
	('CHG049', 12), -- 4 × 3
	('CHG050',  4), -- 16 ÷ 4
	('CHG051', 12), -- 5 + 7
	('CHG052',  4), -- 9 - 5	
	('CHG053', 12), -- 2 × 6
	('CHG054',  7), -- 14 ÷ 2
	('CHG055', 11), -- 8 + 3
	('CHG056',  4), -- 10 - 6
	('CHG057', 12), -- 3 × 4
	('CHG058',  3), -- 18 ÷ 6
	('CHG059', 13), -- 6 + 7
	('CHG060',  8); -- 11 - 3


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
	-- CH101 (ĐÚNG ở DA2003)
	('DA2001','CH101',N'C',0),
	('DA2002','CH101',N'D',0),
	('DA2003','CH101',N'B',1),
	('DA2004','CH101',N'A',0),

	-- CH102 (ĐÚNG ở DA2006)
	('DA2005','CH102',N'B',0),
	('DA2006','CH102',N'C',1),
	('DA2007','CH102',N'D',0),
	('DA2008','CH102',N'E',0),

	-- CH103 (ĐÚNG ở DA2009)  ✅ SỬA LỖI LỆCH ĐÁP ÁN TRƯỚC ĐÂY
	('DA2009','CH103',N'29',1),
	('DA2010','CH103',N'26',0),
	('DA2011','CH103',N'28',0),
	('DA2012','CH103',N'30',0),

	-- CH104 (ĐÚNG ở DA2015)
	('DA2013','CH104',N'A',0),
	('DA2014','CH104',N'B',0),
	('DA2015','CH104',N'C',1),
	('DA2016','CH104',N'D',0),

	-- CH105 (ĐÚNG ở DA2018)
	('DA2017','CH105',N'X',0),
	('DA2018','CH105',N'Y',1),
	('DA2019','CH105',N'Z',0),
	('DA2020','CH105',N'W',0),

	-- CH106 (ĐÚNG ở DA2021)
	('DA2021','CH106',N'A, B, C',1),
	('DA2022','CH106',N'1, 2, 3',0),
	('DA2023','CH106',N'+, -, ×',0),
	('DA2024','CH106',N'@, #, $',0),

	-- CH107 (ĐÚNG ở DA2028)
	('DA2025','CH107',N'A',0),
	('DA2026','CH107',N'C',0),
	('DA2027','CH107',N'D',0),
	('DA2028','CH107',N'B',1),

	-- CH108 (ĐÚNG ở DA2029)
	('DA2029','CH108',N'Chữ cái đầu của tên riêng',1),
	('DA2030','CH108',N'Chữ cái cuối của tên riêng',0),
	('DA2031','CH108',N'Chữ cái ở giữa tên riêng',0),
	('DA2032','CH108',N'Không cần viết hoa',0),

	-- CH109 (ĐÚNG ở DA2034)
	('DA2033','CH109',N'L',0),
	('DA2034','CH109',N'O',1),
	('DA2035','CH109',N'T',0),
	('DA2036','CH109',N'E',0),

	-- CH110 (ĐÚNG ở DA2037)
	('DA2037','CH110',N'O',1),
	('DA2038','CH110',N'N',0),
	('DA2039','CH110',N'M',0),
	('DA2040','CH110',N'H',0),

	-- =========================================================
	-- CC012 – VẦN ÂM (CH111–CH120)

	-- CH111 (ĐÚNG ở DA2044)
	('DA2041','CH111',N'en',0),
	('DA2042','CH111',N'in',0),
	('DA2043','CH111',N'un',0),
	('DA2044','CH111',N'an',1),

	-- CH112 (ĐÚNG ở DA2046)
	('DA2045','CH112',N'ung',0),
	('DA2046','CH112',N'ong',1),
	('DA2047','CH112',N'eng',0),
	('DA2048','CH112',N'ang',0),

	-- CH113 (ĐÚNG ở DA2051)
	('DA2049','CH113',N'bàn',0),
	('DA2050','CH113',N'ban',0),
	('DA2051','CH113',N'bát',1),
	('DA2052','CH113',N'báo',0),

	-- CH114 (ĐÚNG ở DA2055)
	('DA2053','CH114',N'đan',0),
	('DA2054','CH114',N'đen',0),
	('DA2055','CH114',N'đèn',1),
	('DA2056','CH114',N'đơn',0),

	-- CH115 (ĐÚNG ở DA2057)
	('DA2057','CH115',N'an',1),
	('DA2058','CH115',N'at',0),
	('DA2059','CH115',N'am',0),
	('DA2060','CH115',N'ap',0),

	-- CH116 (ĐÚNG ở DA2062)
	('DA2061','CH116',N'ao',0),
	('DA2062','CH116',N'an',1),
	('DA2063','CH116',N'ac',0),
	('DA2064','CH116',N'a',0),

	-- CH117 (ĐÚNG ở DA2068)
	('DA2065','CH117',N'ua',0),
	('DA2066','CH117',N'oe',0),
	('DA2067','CH117',N'oi',0),
	('DA2068','CH117',N'oa',1),

	-- CH118 (ĐÚNG ở DA2069)
	('DA2069','CH118',N'voi',1),
	('DA2070','CH118',N'vua',0),
	('DA2071','CH118',N've',0),
	('DA2072','CH118',N'vai',0),

	-- CH119 (ĐÚNG ở DA2075)
	('DA2073','CH119',N'ia',0),
	('DA2074','CH119',N'ua',0),
	('DA2075','CH119',N'oa',1),
	('DA2076','CH119',N'oe',0),

	-- CH120 (ĐÚNG ở DA2078)
	('DA2077','CH120',N'a',0),
	('DA2078','CH120',N'ê',1),
	('DA2079','CH120',N'i',0),
	('DA2080','CH120',N'o',0),

	-- =========================================================
	-- CC013 – TỪ ĐƠN (CH121–CH125)

	-- CH121 (ĐÚNG ở DA2082)
	('DA2081','CH121',N'bàn học',0),
	('DA2082','CH121',N'bàn',1),
	('DA2083','CH121',N'cái bàn',0),
	('DA2084','CH121',N'bàn ghế',0),

	-- CH122 (ĐÚNG ở DA2086)
	('DA2085','CH122',N'cô giáo',0),
	('DA2086','CH122',N'mẹ',1),
	('DA2087','CH122',N'bạn bè',0),
	('DA2088','CH122',N'bác sĩ',0),

	-- CH123 (ĐÚNG ở DA2089)
	('DA2089','CH123',N'bút',1),
	('DA2090','CH123',N'bút chì',0),
	('DA2091','CH123',N'hộp bút',0),
	('DA2092','CH123',N'cái bút',0),

	-- CH124 (ĐÚNG ở DA2095)
	('DA2093','CH124',N'con mèo',0),
	('DA2094','CH124',N'mèo con',0),
	('DA2095','CH124',N'mèo',1),
	('DA2096','CH124',N'mèo mun',0),

	-- CH125 (ĐÚNG ở DA2099)
	('DA2097','CH125',N'cây cối',0),
	('DA2098','CH125',N'con sông',0),
	('DA2099','CH125',N'nhà',1),
	('DA2100','CH125',N'bầu trời',0),

	('DA2101','CH126',N'bàn ghế',0),
	('DA2102','CH126',N'cái bàn',0),
	('DA2103','CH126',N'bút',1),
	('DA2104','CH126',N'học sinh',0),

	-- CH127 (ĐÚNG ở DA2106)
	('DA2105','CH127',N'cây cối',0),
	('DA2106','CH127',N'mèo',1),
	('DA2107','CH127',N'con mèo',0),
	('DA2108','CH127',N'mèo con',0),

	-- CH128 (ĐÚNG ở DA2109)
	('DA2109','CH128',N'bố',1),
	('DA2110','CH128',N'ông bà',0),
	('DA2111','CH128',N'gia đình',0),
	('DA2112','CH128',N'anh chị',0),

	-- CH129 (ĐÚNG ở DA2115)
	('DA2113','CH129',N'hộp bút',0),
	('DA2114','CH129',N'cặp sách',0),
	('DA2115','CH129',N'bút',1),
	('DA2116','CH129',N'bàn học',0),

	-- CH130 (ĐÚNG ở DA2119)
	('DA2117','CH130',N'bầu trời',0),
	('DA2118','CH130',N'dòng sông',0),
	('DA2119','CH130',N'mây',1),
	('DA2120','CH130',N'ngọn núi',0),

	-- =========================================================
	-- CC014 – TỪ GHÉP (CH131–CH140)

	-- CH131 (ĐÚNG ở DA2122)
	('DA2121','CH131',N'bàn',0),
	('DA2122','CH131',N'học sinh',1),
	('DA2123','CH131',N'sách',0),
	('DA2124','CH131',N'bút',0),

	-- CH132 (ĐÚNG ở DA2125)
	('DA2125','CH132',N'bàn ghế',1),
	('DA2126','CH132',N'bàn',0),
	('DA2127','CH132',N'ghế',0),
	('DA2128','CH132',N'ngồi',0),

	-- CH133 (ĐÚNG ở DA2130)
	('DA2129','CH133',N'bé',0),
	('DA2130','CH133',N'học sinh',1),
	('DA2131','CH133',N'cô',0),
	('DA2132','CH133',N'anh',0),

	-- CH134 (ĐÚNG ở DA2135)
	('DA2133','CH134',N'nhà',0),
	('DA2134','CH134',N'lớp',0),
	('DA2135','CH134',N'sân trường',1),
	('DA2136','CH134',N'bàn ghế',0),

	-- CH135 (ĐÚNG ở DA2138)
	('DA2137','CH135',N'mèo',0),
	('DA2138','CH135',N'xe đạp',1),
	('DA2139','CH135',N'ăn',0),
	('DA2140','CH135',N'chạy',0),

	-- CH136 (ĐÚNG ở DA2141)
	('DA2141','CH136',N'học tập',1),
	('DA2142','CH136',N'học',0),
	('DA2143','CH136',N'viết',0),
	('DA2144','CH136',N'đọc',0),

	-- CH137 (ĐÚNG ở DA2147)
	('DA2145','CH137',N'bé',0),
	('DA2146','CH137',N'con mèo',0),
	('DA2147','CH137',N'gia đình',1),
	('DA2148','CH137',N'chạy nhảy',0),

	-- CH138 (ĐÚNG ở DA2152)
	('DA2149','CH138',N'học sinh',0),
	('DA2150','CH138',N'bàn ghế',0),
	('DA2151','CH138',N'sân trường',0),
	('DA2152','CH138',N'ăn',1),

	-- CH139 (ĐÚNG ở DA2154)
	('DA2153','CH139',N'ông',0),
	('DA2154','CH139',N'ông bà',1),
	('DA2155','CH139',N'bố',0),
	('DA2156','CH139',N'mẹ',0),

	-- CH140 (ĐÚNG ở DA2158)
	('DA2157','CH140',N'ăn cơm',0),
	('DA2158','CH140',N'học tập',1),
	('DA2159','CH140',N'chạy nhảy',0),
	('DA2160','CH140',N'ngủ nghỉ',0),

	-- =========================================================
	-- CC015 – CÂU ĐƠN (CH141–CH150)

	-- CH141 (ĐÚNG ở DA2162)
	('DA2161','CH141',N'Bé và mẹ đi chợ.',0),
	('DA2162','CH141',N'Bé đi học.',1),
	('DA2163','CH141',N'Bé đi học và mẹ nấu cơm.',0),
	('DA2164','CH141',N'Bé học bài rồi đi ngủ.',0),

	-- CH142 (ĐÚNG ở DA2167)
	('DA2165','CH142',N'Đi học.',0),
	('DA2166','CH142',N'Bé học.',0),
	('DA2167','CH142',N'Bé đang học bài.',1),
	('DA2168','CH142',N'Học bài.',0),

	-- CH143 (ĐÚNG ở DA2170)
	('DA2169','CH143',N'Bạn có khỏe không?',0),
	('DA2170','CH143',N'Bé đang học bài.',1),
	('DA2171','CH143',N'Ôi đẹp quá!',0),
	('DA2172','CH143',N'Đi thôi!',0),

	-- CH144 (ĐÚNG ở DA2175)
	('DA2173','CH144',N'Con mèo rất xinh.',0),
	('DA2174','CH144',N'Bé là học sinh.',0),
	('DA2175','CH144',N'Bé đang chạy.',1),
	('DA2176','CH144',N'Ôi đẹp quá!',0),

	-- CH145 (ĐÚNG ở DA2178)
	('DA2177','CH145',N'Bé đang chạy.',0),
	('DA2178','CH145',N'Cái bàn rất sạch.',1),
	('DA2179','CH145',N'Bé đi học.',0),
	('DA2180','CH145',N'Bé ăn cơm.',0),

	-- CH146 (ĐÚNG ở DA2183)
	('DA2181','CH146',N'Bé và Lan đi học.',0),
	('DA2182','CH146',N'Lan và Hoa học bài.',0),
	('DA2183','CH146',N'Bé học bài.',1),
	('DA2184','CH146',N'Bé học và Lan viết.',0),

	-- CH147 (ĐÚNG ở DA2188)
	('DA2185','CH147',N'Bé đang học bài.',0),
	('DA2186','CH147',N'Bé là học sinh.',0),
	('DA2187','CH147',N'Bé rất ngoan.',0),
	('DA2188','CH147',N'Bé học.',1),

	-- CH148 (ĐÚNG ở DA2190)
	('DA2189','CH148',N'Bé và mẹ đi chợ.',0),
	('DA2190','CH148',N'Bé đi học.',1),
	('DA2191','CH148',N'Bé đi học và mẹ nấu cơm.',0),
	('DA2192','CH148',N'Bé học bài rồi ngủ.',0),

	-- CH149 (ĐÚNG ở DA2195)
	('DA2193','CH149',N'Đi học.',0),
	('DA2194','CH149',N'Bé học.',0),
	('DA2195','CH149',N'Bé đang học bài.',1),
	('DA2196','CH149',N'Học bài.',0),

	-- CH150 (ĐÚNG ở DA2199)
	('DA2197','CH150',N'Ôi đẹp quá!',0),
	('DA2198','CH150',N'Bạn khỏe không?',0),
	('DA2199','CH150',N'Bé kể chuyện.',1),
	('DA2200','CH150',N'Đi thôi!',0),

    -- CH151 (ĐÚNG: DA2202)
	('DA2201','CH151',N'Dấu phẩy (,)',0),
	('DA2202','CH151',N'Dấu chấm (.)',1),
	('DA2203','CH151',N'Dấu hỏi (?)',0),
	('DA2204','CH151',N'Dấu chấm than (!)',0),

	-- CH152 (ĐÚNG: DA2207)
	('DA2205','CH152',N'Dấu chấm (.)',0),
	('DA2206','CH152',N'Dấu phẩy (,)',0),
	('DA2207','CH152',N'Dấu hỏi (?)',1),
	('DA2208','CH152',N'Dấu chấm than (!)',0),

	-- CH153 (ĐÚNG: DA2210)
	('DA2209','CH153',N'Dấu chấm (.)',0),
	('DA2210','CH153',N'Dấu chấm than (!)',1),
	('DA2211','CH153',N'Dấu phẩy (,)',0),
	('DA2212','CH153',N'Dấu hỏi (?)',0),

	-- CH154 (ĐÚNG: DA2215)
	('DA2213','CH154',N'Dấu chấm (.)',0),
	('DA2214','CH154',N'Dấu hỏi (?)',0),
	('DA2215','CH154',N'Dấu phẩy (,)',1),
	('DA2216','CH154',N'Dấu chấm than (!)',0),

	-- CH155 (ĐÚNG: DA2219)
	('DA2217','CH155',N'Em học giỏi.',0),
	('DA2218','CH155',N'Em học, giỏi',0),
	('DA2219','CH155',N'Em học, giỏi.',1),
	('DA2220','CH155',N'Em, học giỏi.',0),

	-- CH156 (ĐÚNG: DA2222)
	('DA2221','CH156',N'Dấu phẩy (,)',0),
	('DA2222','CH156',N'Dấu chấm (.)',1),
	('DA2223','CH156',N'Dấu hỏi (?)',0),
	('DA2224','CH156',N'Dấu chấm than (!)',0),

	-- CH157 (ĐÚNG: DA2227)
	('DA2225','CH157',N'Dấu chấm (.)',0),
	('DA2226','CH157',N'Dấu phẩy (,)',0),
	('DA2227','CH157',N'Dấu hỏi (?)',1),
	('DA2228','CH157',N'Dấu chấm than (!)',0),

	-- CH158 (ĐÚNG: DA2231)
	('DA2229','CH158',N'Dấu chấm (.)',0),
	('DA2230','CH158',N'Dấu phẩy (,)',0),
	('DA2231','CH158',N'Dấu chấm than (!)',1),
	('DA2232','CH158',N'Dấu hỏi (?)',0),

	-- CH159 (ĐÚNG: DA2234)
	('DA2233','CH159',N'Bé học bài',0),
	('DA2234','CH159',N'Bé học bài.',1),
	('DA2235','CH159',N'Bé học, bài',0),
	('DA2236','CH159',N'Bé, học bài.',0),

	-- CH160 (ĐÚNG: DA2239)
	('DA2237','CH160',N'Dấu hai chấm (:)',0),
	('DA2238','CH160',N'Dấu chấm phẩy (;)',0),
	('DA2239','CH160',N'Dấu chấm (.)',1),
	('DA2240','CH160',N'Dấu gạch ngang (-)',0),

	-- =========================================================
	-- CC017 – ĐỒNG ÂM (CH161–CH170)

	-- CH161 (ĐÚNG: DA2242)
	('DA2241','CH161',N'cá – tôm',0),
	('DA2242','CH161',N'ba (bố) – ba (số 3)',1),
	('DA2243','CH161',N'bàn – ghế',0),
	('DA2244','CH161',N'ăn – uống',0),

	-- CH162 (ĐÚNG: DA2246)
	('DA2245','CH162',N'cao – thấp',0),
	('DA2246','CH162',N'cân (nặng) – cân (dụng cụ)',1),
	('DA2247','CH162',N'đỏ – xanh',0),
	('DA2248','CH162',N'lớn – nhỏ',0),

	-- CH163 (ĐÚNG: DA2250)
	('DA2249','CH163',N'ăn – uống',0),
	('DA2250','CH163',N'đá (hòn đá) – đá (đá bóng)',1),
	('DA2251','CH163',N'bút – thước',0),
	('DA2252','CH163',N'sách – vở',0),

	-- CH164 (ĐÚNG: DA2254)
	('DA2253','CH164',N'chạy – nhảy',0),
	('DA2254','CH164',N'câu (cá) – câu (câu văn)',1),
	('DA2255','CH164',N'học – viết',0),
	('DA2256','CH164',N'ngủ – nghỉ',0),

	-- CH165 (ĐÚNG: DA2259)
	('DA2257','CH165',N'cao – thấp',0),
	('DA2258','CH165',N'đẹp – xấu',0),
	('DA2259','CH165',N'mực (viết) – mực (con mực)',1),
	('DA2260','CH165',N'trắng – đen',0),

	-- CH166 (ĐÚNG: DA2262)
	('DA2261','CH166',N'ăn – học',0),
	('DA2262','CH166',N'lá (cây) – lá (thư)',1),
	('DA2263','CH166',N'chạy – đi',0),
	('DA2264','CH166',N'đọc – viết',0),

	-- CH167 (ĐÚNG: DA2267)
	('DA2265','CH167',N'Giúp hiểu nhanh hơn',0),
	('DA2266','CH167',N'Làm câu văn dài hơn',0),
	('DA2267','CH167',N'Gây nhầm lẫn nghĩa',1),
	('DA2268','CH167',N'Làm câu ngắn lại',0),

	-- CH168 (ĐÚNG: DA2271)
	('DA2269','CH168',N'ăn',0),
	('DA2270','CH168',N'ngủ',0),
	('DA2271','CH168',N'câu',1),
	('DA2272','CH168',N'chạy',0),

	-- CH169 (ĐÚNG: DA2274)
	('DA2273','CH169',N'Cách viết',0),
	('DA2274','CH169',N'Nghĩa của từ',1),
	('DA2275','CH169',N'Số chữ cái',0),
	('DA2276','CH169',N'Độ dài từ',0),

	-- CH170 (ĐÚNG: DA2279)
	('DA2277','CH170',N'Cách viết',0),
	('DA2278','CH170',N'Cách phát âm',0),
	('DA2279','CH170',N'Nghĩa của từ',1),
	('DA2280','CH170',N'Số chữ cái',0),

	-- =========================================================
	-- CC018 – TRÁI NGHĨA (CH171–CH180)

	-- CH171 (ĐÚNG: DA2283)
	('DA2281','CH171',N'dài',0),
	('DA2282','CH171',N'lớn',0),
	('DA2283','CH171',N'thấp',1),
	('DA2284','CH171',N'rộng',0),

	-- CH172 (ĐÚNG: DA2288)
	('DA2285','CH172',N'mạnh',0),
	('DA2286','CH172',N'vội',0),
	('DA2287','CH172',N'lẹ',0),
	('DA2288','CH172',N'chậm',1),

	-- CH173 (ĐÚNG: DA2290)
	('DA2289','CH173',N'cao – to',0),
	('DA2290','CH173',N'nóng – lạnh',1),
	('DA2291','CH173',N'đẹp – xinh',0),
	('DA2292','CH173',N'vui – cười',0),

	-- CH174 (ĐÚNG: DA2295)
	('DA2293','CH174',N'hạnh phúc',0),
	('DA2294','CH174',N'cười',0),
	('DA2295','CH174',N'buồn',1),
	('DA2296','CH174',N'thích',0),

	-- CH175 (ĐÚNG: DA2299)
	('DA2297','CH175',N'sáng',0),
	('DA2298','CH175',N'trắng',0),
	('DA2299','CH175',N'bẩn',1),
	('DA2300','CH175',N'mát',0),

	   -- CH176: Trái nghĩa với “to”?  (ĐÚNG ở DA2303)
	('DA2301', 'CH176', N'lớn', 0),
	('DA2302', 'CH176', N'rộng', 0),
	('DA2303', 'CH176', N'nhỏ', 1),
	('DA2304', 'CH176', N'dài', 0),

	-- CH177: Cặp từ nào có nghĩa trái ngược?  (ĐÚNG ở DA2305)
	('DA2305', 'CH177', N'nóng – lạnh', 1),
	('DA2306', 'CH177', N'ăn – uống', 0),
	('DA2307', 'CH177', N'học – viết', 0),
	('DA2308', 'CH177', N'vui – cười', 0),

	-- CH178: Trái nghĩa với “sáng”?  (ĐÚNG ở DA2311)
	('DA2309', 'CH178', N'đẹp', 0),
	('DA2310', 'CH178', N'trắng', 0),
	('DA2311', 'CH178', N'tối', 1),
	('DA2312', 'CH178', N'vui', 0),

	-- CH179: Trái nghĩa với “nóng”?  (ĐÚNG ở DA2316)
	('DA2313', 'CH179', N'ấm', 0),
	('DA2314', 'CH179', N'sáng', 0),
	('DA2315', 'CH179', N'đỏ', 0),
	('DA2316', 'CH179', N'lạnh', 1),

	-- CH180: Từ trái nghĩa dùng để làm gì?  (ĐÚNG ở DA2319)
	('DA2317', 'CH180', N'Làm câu văn dài hơn', 0),
	('DA2318', 'CH180', N'Làm câu khó hiểu hơn', 0),
	('DA2319', 'CH180', N'Giúp diễn đạt rõ ý bằng các từ đối lập', 1),
	('DA2320', 'CH180', N'Chỉ để trang trí câu', 0),

	-- =========================================================
	-- CC019 – CỦNG CỐ THÀNH NGỮ (CH181–CH190)

	-- CH181: Thành ngữ nói về công ơn cha mẹ?  (ĐÚNG ở DA2324)
	('DA2321', 'CH181', N'Có công mài sắt, có ngày nên kim', 0),
	('DA2322', 'CH181', N'Đi một ngày đàng, học một sàng khôn', 0),
	('DA2323', 'CH181', N'Một cây làm chẳng nên non', 0),
	('DA2324', 'CH181', N'Công cha như núi Thái Sơn', 1),

	-- CH182: Thành ngữ khuyên con người chăm chỉ?  (ĐÚNG ở DA2327)
	('DA2325', 'CH182', N'Uống nước nhớ nguồn', 0),
	('DA2326', 'CH182', N'Bầu ơi thương lấy bí cùng', 0),
	('DA2327', 'CH182', N'Có công mài sắt, có ngày nên kim', 1),
	('DA2328', 'CH182', N'Tốt gỗ hơn tốt nước sơn', 0),

	-- CH183: Thành ngữ nói về tình bạn?  (ĐÚNG ở DA2332)
	('DA2329', 'CH183', N'Ăn quả nhớ kẻ trồng cây', 0),
	('DA2330', 'CH183', N'Có chí thì nên', 0),
	('DA2331', 'CH183', N'Gần mực thì đen', 0),
	('DA2332', 'CH183', N'Bầu ơi thương lấy bí cùng', 1),

	-- CH184: Thành ngữ dùng để khen người tốt?  (ĐÚNG ở DA2335)
	('DA2333', 'CH184', N'Có mới nới cũ', 0),
	('DA2334', 'CH184', N'Đi guốc trong bụng', 0),
	('DA2335', 'CH184', N'Tốt gỗ hơn tốt nước sơn', 1),
	('DA2336', 'CH184', N'Gió chiều nào theo chiều ấy', 0),

	-- CH185: Thành ngữ nói về học tập?  (ĐÚNG ở DA2337)
	('DA2337', 'CH185', N'Đi một ngày đàng, học một sàng khôn', 1),
	('DA2338', 'CH185', N'Uống nước nhớ nguồn', 0),
	('DA2339', 'CH185', N'Bầu ơi thương lấy bí cùng', 0),
	('DA2340', 'CH185', N'Ăn quả nhớ kẻ trồng cây', 0),

	-- CH186: Thành ngữ nói về đoàn kết?  (ĐÚNG ở DA2343)
	('DA2341', 'CH186', N'Có chí thì nên', 0),
	('DA2342', 'CH186', N'Gần mực thì đen', 0),
	('DA2343', 'CH186', N'Một cây làm chẳng nên non', 1),
	('DA2344', 'CH186', N'Có công mài sắt, có ngày nên kim', 0),

	-- CH187: Thành ngữ dùng để khuyên nhủ?  (ĐÚNG ở DA2346)
	('DA2345', 'CH187', N'Bầu ơi thương lấy bí cùng', 0),
	('DA2346', 'CH187', N'Có chí thì nên', 1),
	('DA2347', 'CH187', N'Gió chiều nào theo chiều ấy', 0),
	('DA2348', 'CH187', N'Đi guốc trong bụng', 0),

	-- CH188: Thành ngữ nói về lòng biết ơn?  (ĐÚNG ở DA2351)
	('DA2349', 'CH188', N'Có công mài sắt, có ngày nên kim', 0),
	('DA2350', 'CH188', N'Tốt gỗ hơn tốt nước sơn', 0),
	('DA2351', 'CH188', N'Ăn quả nhớ kẻ trồng cây', 1),
	('DA2352', 'CH188', N'Gần mực thì đen', 0),

	-- CH189: Thành ngữ dùng trong giao tiếp?  (ĐÚNG ở DA2355)
	('DA2353', 'CH189', N'Ăn quả nhớ kẻ trồng cây', 0),
	('DA2354', 'CH189', N'Có công mài sắt, có ngày nên kim', 0),
	('DA2355', 'CH189', N'Lời nói chẳng mất tiền mua', 1),
	('DA2356', 'CH189', N'Một cây làm chẳng nên non', 0),

	-- CH190: Thành ngữ thường dùng để làm gì?  (ĐÚNG ở DA2358)
	('DA2357', 'CH190', N'Chỉ để viết dài hơn', 0),
	('DA2358', 'CH190', N'Giúp lời nói hay, ngắn gọn và giàu ý nghĩa', 1),
	('DA2359', 'CH190', N'Để thay thế mọi từ trong câu', 0),
	('DA2360', 'CH190', N'Để làm câu khó hiểu hơn', 0),

	-- =========================================================
	-- CC020 – CỦNG CỐ THƠ (CH191–CH200)

	-- CH191: Thơ thường viết theo hình thức nào?  (ĐÚNG ở DA2362)
	('DA2361', 'CH191', N'Viết như văn xuôi', 0),
	('DA2362', 'CH191', N'Có vần điệu', 1),
	('DA2363', 'CH191', N'Không có nhịp', 0),
	('DA2364', 'CH191', N'Chỉ có 1 câu', 0),

	-- CH192: Thơ thường dùng để làm gì?  (ĐÚNG ở DA2368)
	('DA2365', 'CH192', N'Chỉ để kể chuyện dài', 0),
	('DA2366', 'CH192', N'Chỉ để hướng dẫn làm bài', 0),
	('DA2367', 'CH192', N'Chỉ để ghi chú', 0),
	('DA2368', 'CH192', N'Bộc lộ cảm xúc và suy nghĩ', 1),

	-- CH193: Từ ngữ trong thơ thường có đặc điểm gì?  (ĐÚNG ở DA2369)
	('DA2369', 'CH193', N'Giàu hình ảnh và cảm xúc', 1),
	('DA2370', 'CH193', N'Khô khan, giống công thức', 0),
	('DA2371', 'CH193', N'Chỉ gồm số và ký hiệu', 0),
	('DA2372', 'CH193', N'Không có ý nghĩa', 0),

	-- CH194: Thơ thường có yếu tố nào?  (ĐÚNG ở DA2374)
	('DA2373', 'CH194', N'Chỉ có đoạn văn dài', 0),
	('DA2374', 'CH194', N'Vần và nhịp', 1),
	('DA2375', 'CH194', N'Chỉ có hình vẽ', 0),
	('DA2376', 'CH194', N'Không có âm điệu', 0),

	-- CH195: Thơ giúp con cảm nhận điều gì?  (ĐÚNG ở DA2380)
	('DA2377', 'CH195', N'Chỉ số liệu', 0),
	('DA2378', 'CH195', N'Quy tắc', 0),
	('DA2379', 'CH195', N'Chỉ bài học khô khan', 0),
	('DA2380', 'CH195', N'Cảm xúc và vẻ đẹp của cuộc sống', 1),

	-- CH196: Câu thơ dùng để làm gì?  (ĐÚNG ở DA2382)
	('DA2381', 'CH196', N'Chỉ để liệt kê', 0),
	('DA2382', 'CH196', N'Thể hiện ý thơ', 1),
	('DA2383', 'CH196', N'Chỉ để ghi công thức', 0),
	('DA2384', 'CH196', N'Chỉ để kể chuyện dài', 0),

	-- CH197: Thơ thường có nhịp điệu như thế nào?  (ĐÚNG ở DA2387)
	('DA2385', 'CH197', N'Rất khó nhớ', 0),
	('DA2386', 'CH197', N'Không có nhịp', 0),
	('DA2387', 'CH197', N'Nhịp nhàng, dễ nhớ', 1),
	('DA2388', 'CH197', N'Luôn rất chậm', 0),

	-- CH198: Khi đọc thơ cần chú ý điều gì?  (ĐÚNG ở DA2392)
	('DA2389', 'CH198', N'Đọc thật nhanh', 0),
	('DA2390', 'CH198', N'Đọc không cần ngắt', 0),
	('DA2391', 'CH198', N'Chỉ đọc thật to', 0),
	('DA2392', 'CH198', N'Đọc đúng nhịp và ngắt nghỉ', 1),

	-- CH199: Thơ giúp con học được điều gì?  (ĐÚNG ở DA2394)
	('DA2393', 'CH199', N'Chỉ phép tính', 0),
	('DA2394', 'CH199', N'Cách dùng từ hay và cảm xúc', 1),
	('DA2395', 'CH199', N'Chỉ luật chơi', 0),
	('DA2396', 'CH199', N'Chỉ công thức', 0),

	-- CH200: Đọc thơ giúp con phát triển điều gì?  (ĐÚNG ở DA2397)
	('DA2397', 'CH200', N'Ngôn ngữ và cảm xúc', 1),
	('DA2398', 'CH200', N'Tốc độ chạy', 0),
	('DA2399', 'CH200', N'Sức mạnh', 0),
	('DA2400', 'CH200', N'Khả năng tính toán', 0),

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
----------------------------insert Tính toán liên hoàn---------------------------------
GO
DECLARE @ch INT = 41;
DECLARE @thuTu INT = 1;

WHILE @ch <= 60
BEGIN
    INSERT INTO HoatDong_CauHoi (MaHoatDong, MaCauHoi, ThuTu)
    VALUES (
        'TC001',
        'CHG' + RIGHT('000' + CAST(@ch AS VARCHAR(3)), 3),
        @thuTu
    );

    SET @ch = @ch + 1;
    SET @thuTu = @thuTu + 1;
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
WHERE h.TieuDe = N'Hoàn thiện câu từ' and d.LaDapAnDung=1
ORDER BY c.MaCauHoi, d.MaDapAn;
select * from LoaiHoatDong
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
WHERE h.TieuDe = N'Liên hoàn tính toán' and d.LaDapAnDung=1
ORDER BY c.MaCauHoi, d.MaDapAn;