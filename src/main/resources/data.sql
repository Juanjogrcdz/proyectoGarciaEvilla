-- =====================================================================
-- DATOS INICIALES (se ejecuta en cada arranque; es idempotente)
-- =====================================================================

-- ROLES: los ids son FIJOS porque UserUseCase los usa escritos a mano
-- (PROPIETARIO = 2, EMPLEADO = 3, CLIENTE = 4). No cambies este orden.
MERGE INTO roles (id, name, description) KEY (id) VALUES (1, 'ROLE_ADMINISTRADOR', 'Administrador de la plazoleta');
MERGE INTO roles (id, name, description) KEY (id) VALUES (2, 'ROLE_PROPIETARIO',   'Propietario de restaurante');
MERGE INTO roles (id, name, description) KEY (id) VALUES (3, 'ROLE_EMPLEADO',      'Empleado de restaurante');
MERGE INTO roles (id, name, description) KEY (id) VALUES (4, 'ROLE_CLIENTE',       'Cliente de la plazoleta');

-- ADMINISTRADOR INICIAL (no existe endpoint público para crearlo)
--   correo: admin@plazoleta.com
--   clave : Admin123*   (guardada como hash BCrypt)
MERGE INTO users (name, last_name, identity_document, phone, birth_date, email, password, role_id)
    KEY (email)
    VALUES ('Admin', 'Plazoleta', '1000000000', '+573000000000', DATE '1990-01-01',
            'admin@plazoleta.com',
            '$2a$10$4z0Hm74GMjvtb5MCSqHrtukOa5v8Mq5jZTuuX3FMU2TMES2xJuVDu',
            1);
