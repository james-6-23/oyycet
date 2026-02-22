# cet4-practice-backend

免费英语四级练习站后端（Spring Boot 3 / Java 17+）。

## 技术栈
- Java 17+
- Spring Boot 3.x
- MyBatis Plus
- MySQL 8.0
- JWT（jjwt）
- Flyway（数据库迁移：`flyway-core` + `flyway-mysql`）

## 本地运行

### 1) 准备 MySQL
创建数据库：`cet_practice`（字符集 `utf8mb4`）。

如你有 Docker，可在本目录运行：
```bash
docker compose up -d
```

### 2) 配置环境变量（或直接改 `application.yml`）
- `DB_URL`（示例：`jdbc:mysql://localhost:3306/cet_practice?...`）
- `DB_USERNAME`
- `DB_PASSWORD`
- `APP_JWT_SECRET`（至少 32 字节）

### 3) 启动
```bash
mvn -q spring-boot:run
```

## API 概览
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`（需要 Bearer Token）

- `GET /api/papers`
- `GET /api/papers/{id}`
- `GET /api/papers/{id}/questions?mode=practice|exam`
- `POST /api/papers/{id}/submit`（需要 Bearer Token）

- `GET /api/me/practice-records`（需要 Bearer Token）
- `GET /api/me/practice-records/{id}`（需要 Bearer Token）
- `GET /api/me/practice-records/stats`（需要 Bearer Token）

- `POST /api/admin/papers/import-json`（需要 ADMIN 角色）
- `POST /api/admin/papers/{id}/publish`（需要 ADMIN 角色）
- `POST /api/admin/papers/{id}/unpublish`（需要 ADMIN 角色）

## 备注
- 数据表名使用 `cet_` 前缀（避免与 SmartCET 的 `exam/question/exam_record` 重名）。
- 如你在仓库里看到 `cet4-practice-backend/.tmp/mysql-data`，这是本地验证 DDL 时生成的 MySQL 临时数据目录，可手动删除。
