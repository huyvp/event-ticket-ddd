-- ============================================
-- CREATE DATABASE
-- ============================================
IF
NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'vetautet')
BEGIN
    CREATE
DATABASE vetautet;
END
GO

USE vetautet;
GO

-- 1. ticket table
IF OBJECT_ID('ticket', 'U') IS NULL
BEGIN
CREATE TABLE ticket
(
    id   BIGINT IDENTITY(1,1) NOT NULL, -- Primary key
    name NVARCHAR(50) NOT NULL,         -- ticket name
    [
    desc]
    NVARCHAR
(
    MAX
) NULL, -- ticket description
    start_time DATETIME2(0) NOT NULL, -- ticket sale start time
    end_time DATETIME2(0) NOT NULL, -- ticket sale end time
    status INT NOT NULL DEFAULT 0, -- ticket sale activity status, -- 0: deactive, 1: activity
    updated_at DATETIME2(0) NOT NULL DEFAULT SYSUTCDATETIME(), -- Last update time
    created_at DATETIME2(0) NOT NULL DEFAULT SYSUTCDATETIME(), -- Creation time
    PRIMARY KEY (id)
    );
END
GO

-- Very high query runtime
CREATE INDEX idx_end_time ON ticket (end_time);
-- Very high query runtime
CREATE INDEX idx_start_time ON ticket (start_time);
-- Very high query runtime
CREATE INDEX idx_status ON ticket (status);
GO

-- 2. ticket detail (item) table
IF OBJECT_ID('ticket_item', 'U') IS NULL
BEGIN
CREATE TABLE ticket_item
(
    id   BIGINT IDENTITY(1,1) NOT NULL, -- Primary key
    name NVARCHAR(50) NOT NULL,         -- Ticket title
    [
    description]
    NVARCHAR
(
    MAX
) NULL, -- Ticket description
    stock_initial INT NOT NULL DEFAULT 0, -- Initial stock quantity (e.g., 1000 tickets)
    stock_available INT NOT NULL DEFAULT 0, -- Current available stock (e.g., 900 tickets)
    is_stock_prepared BIT NOT NULL DEFAULT 0, -- Indicates if stock is pre-warmed (0/1), -- warm up cache
    price_original BIGINT NOT NULL, -- Original ticket price, -- Giá gốc: ví dụ: 100K/ticket
    price_flash BIGINT NOT NULL, -- Discounted price during flash sale, -- Giảm giá khung giờ vàng : ví dụ: 10K/ticket
    sale_start_time DATETIME2(0) NOT NULL, -- Flash sale start time
    sale_end_time DATETIME2(0) NOT NULL, -- Flash sale end time
    status INT NOT NULL DEFAULT 0, -- Ticket status (e.g., active/inactive), -- Trạng thái của vé (ví dụ: hoạt động/không hoạt động)
    activity_id BIGINT NOT NULL, -- ID of associated activity, -- ID của hoạt động liên quan đến vé
    updated_at DATETIME2(0) NOT NULL DEFAULT SYSUTCDATETIME(), -- Timestamp of the last update
    created_at DATETIME2(0) NOT NULL DEFAULT SYSUTCDATETIME(), -- Creation timestamp
    PRIMARY KEY (id)
    );
END
GO

CREATE INDEX idx_end_time ON ticket_item (sale_end_time);
CREATE INDEX idx_start_time ON ticket_item (sale_start_time);
CREATE INDEX idx_status ON ticket_item (status);
GO

-- INSERT MOCK DATA
-- Insert data into `ticket` table
INSERT INTO ticket (name, [desc], start_time, end_time, status, updated_at, created_at)
VALUES
    (N'Đợt Mở Bán Vé Ngày 12/12', N'Sự kiện mở bán vé đặc biệt cho ngày 12/12', '2024-12-12 00:00:00', '2024-12-12 23:59:59', 1, SYSUTCDATETIME(), SYSUTCDATETIME()),
    (N'Đợt Mở Bán Vé Ngày 01/01', N'Sự kiện mở bán vé cho ngày đầu năm mới 01/01', '2025-01-01 00:00:00', '2025-01-01 23:59:59', 1, SYSUTCDATETIME(), SYSUTCDATETIME());
GO

-- Insert data into `ticket_item` table corresponding to each event in `ticket` table
INSERT INTO ticket_item (name, [description], stock_initial, stock_available, is_stock_prepared, price_original, price_flash, sale_start_time, sale_end_time, status, activity_id, updated_at, created_at)
VALUES
    -- Ticket items for the 12/12 event
    (N'Vé Sự Kiện 12/12 - Hạng Phổ Thông', N'Vé phổ thông cho sự kiện ngày 12/12', 1000, 1000, 0, 100000, 10000, '2024-12-12 00:00:00', '2024-12-12 23:59:59', 1, 1, SYSUTCDATETIME(), SYSUTCDATETIME()),
    (N'Vé Sự Kiện 12/12 - Hạng VIP', N'Vé VIP cho sự kiện ngày 12/12', 500, 500, 0, 200000, 15000, '2024-12-12 00:00:00', '2024-12-12 23:59:59', 1, 1, SYSUTCDATETIME(), SYSUTCDATETIME()),

    -- Ticket items for the 01/01 event
    (N'Vé Sự Kiện 01/01 - Hạng Phổ Thông', N'Vé phổ thông cho sự kiện ngày 01/01', 2000, 2000, 0, 100000, 10000, '2025-01-01 00:00:00', '2025-01-01 23:59:59', 1, 2, SYSUTCDATETIME(), SYSUTCDATETIME()),
    (N'Vé Sự Kiện 01/01 - Hạng VIP', N'Vé VIP cho sự kiện ngày 01/01', 1000, 1000, 0, 200000, 15000, '2025-01-01 00:00:00', '2025-01-01 23:59:59', 1, 2, SYSUTCDATETIME(), SYSUTCDATETIME());
GO
