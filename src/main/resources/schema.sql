-- Crear tabla para las Comunidades Autónomas de España
CREATE TABLE IF NOT EXISTS regions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL
);


-- Crear tabla para las provincias españolas
CREATE TABLE IF NOT EXISTS provinces (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    region_id INT NOT NULL,
    FOREIGN KEY (region_id) REFERENCES regions(id)
);

-- Crear la tabla 'supermarket'
CREATE TABLE IF NOT EXISTS supermarkets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Crear la tabla 'location'
CREATE TABLE IF NOT EXISTS locations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    supermarket_id INT,
    province_id INT,
    FOREIGN KEY (supermarket_id) REFERENCES supermarkets(id),
    FOREIGN KEY (province_id) REFERENCES provinces(id)
);

-- Crear la tabla 'categories'
CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    image VARCHAR(500) NULL,
    parent_id INT DEFAULT NULL,
    CONSTRAINT fk_parent_category
        FOREIGN KEY (parent_id) REFERENCES categories(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);


-- Crear la tabla 'tickets'
CREATE TABLE IF NOT EXISTS tickets (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date DATETIME NOT NULL,
    discount DECIMAL(5, 2) NOT NULL,
    location_id INT,
    FOREIGN KEY (location_id) REFERENCES locations(id)
);

-- Crear la tabla 'products'
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category_id INT,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- Crear la tabla 'product_ticket'
CREATE TABLE IF NOT EXISTS product_ticket (
    product_id INT NOT NULL,
    ticket_id INT NOT NULL,
    PRIMARY KEY (product_id, ticket_id),
    FOREIGN KEY (product_id) REFERENCES products(id),
    FOREIGN KEY (ticket_id) REFERENCES tickets(id)
);

