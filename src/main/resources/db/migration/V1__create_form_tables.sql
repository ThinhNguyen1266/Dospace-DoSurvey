-- Flyway Migration V1: Create form tables for dosurvey
-- Converted from MySQL to PostgreSQL syntax

-- Form Category Table
CREATE TABLE IF NOT EXISTS form_category
(
    id            VARCHAR(12)  NOT NULL,
    tenant_id     VARCHAR(12)  NULL,
    name          VARCHAR(64)  NOT NULL,
    description   VARCHAR(255) NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    delete_status VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    created_by    VARCHAR(12)  NULL,
    created_at    TIMESTAMP    NULL,
    updated_by    VARCHAR(12)  NULL,
    updated_at    TIMESTAMP    NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_foc_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_foc_delete_status CHECK (delete_status IN ('ACTIVE', 'DELETED'))
);

CREATE INDEX idx_form_category_name ON form_category (name, status, delete_status);
CREATE INDEX idx_form_category_tenant_id ON form_category (tenant_id, delete_status);

-- Form Table
CREATE TABLE IF NOT EXISTS form
(
    id               VARCHAR(12)  NOT NULL,
    tenant_id        VARCHAR(12)  NULL,
    owner_id         VARCHAR(12)  NOT NULL,
    title            VARCHAR(255) NOT NULL,
    description      TEXT         NULL,
    public_link      VARCHAR(255) NOT NULL UNIQUE,
    editor_link      VARCHAR(255) NULL UNIQUE,
    qr_url           VARCHAR(255) NULL,
    logo_image       VARCHAR(255) NULL,
    banner_image     VARCHAR(255) NULL,
    primary_color    VARCHAR(7)   NULL,
    background_color VARCHAR(7)   NULL,
    header_font      VARCHAR(100) NULL,
    question_font    VARCHAR(100) NULL,
    text_font        VARCHAR(100) NULL,
    ai_accept        BOOLEAN      NOT NULL DEFAULT FALSE,
    ai_response      TEXT         NULL,
    responder_access VARCHAR(30)  NOT NULL DEFAULT 'RESTRICTED',
    editor_access    VARCHAR(30)  NOT NULL DEFAULT 'RESTRICTED',
    editors_can_share BOOLEAN     NOT NULL DEFAULT FALSE,
    category_id      VARCHAR(12)  NULL,
    status           VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    delete_status    VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    close_mode       VARCHAR(20)  NOT NULL DEFAULT 'MANUAL',
    max_response     INTEGER      NULL,
    auto_thanks      BOOLEAN      NOT NULL DEFAULT FALSE,
    valid_from       TIMESTAMP    NULL,
    valid_through    TIMESTAMP    NULL,
    form_for         VARCHAR(20)  NOT NULL DEFAULT 'ALL',
    created_by       VARCHAR(12)  NULL,
    created_at       TIMESTAMP    NULL,
    updated_by       VARCHAR(12)  NULL,
    updated_at       TIMESTAMP    NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_fom_status CHECK (status IN ('DRAFT', 'PUBLIC', 'PRIVATE', 'CLOSED')),
    CONSTRAINT chk_fom_delete_status CHECK (delete_status IN ('ACTIVE', 'DELETED')),
    CONSTRAINT chk_fom_close_mode CHECK (close_mode IN ('MANUAL', 'AUTO')),
    CONSTRAINT chk_fom_form_for CHECK (form_for IN ('ALL', 'EVENT', 'TICKET', 'SURVEY')),
    CONSTRAINT chk_fom_responder_access CHECK (responder_access IN ('RESTRICTED', 'TENANT', 'ANYONE_WITH_LINK')),
    CONSTRAINT chk_fom_editor_access CHECK (editor_access IN ('RESTRICTED', 'TENANT', 'ANYONE_WITH_LINK')),
    CONSTRAINT fk_form_category FOREIGN KEY (category_id)
        REFERENCES form_category (id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE INDEX idx_form_category_id ON form (category_id);
CREATE INDEX idx_form_status ON form (status, delete_status);
CREATE INDEX idx_form_owner_id ON form (owner_id, delete_status);
CREATE INDEX idx_form_tenant_id ON form (tenant_id, delete_status);

-- Form Collaborator Table
CREATE TABLE IF NOT EXISTS form_collaborator
(
    id         VARCHAR(12)  NOT NULL,
    form_id    VARCHAR(12)  NOT NULL,
    account_id VARCHAR(12)  NULL,
    email      VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL,
    expires_at TIMESTAMP    NULL,
    created_by VARCHAR(12)  NULL,
    created_at TIMESTAMP    NULL,
    updated_by VARCHAR(12)  NULL,
    updated_at TIMESTAMP    NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_fco_role CHECK (role IN ('EDITOR', 'RESPONDER')),
    CONSTRAINT fk_collaborator_form FOREIGN KEY (form_id)
        REFERENCES form (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_form_collaborator_form_id ON form_collaborator (form_id);
CREATE INDEX idx_form_collaborator_account_id ON form_collaborator (account_id);

-- Form Page Table
CREATE TABLE IF NOT EXISTS form_page
(
    id          VARCHAR(12)  NOT NULL,
    form_id     VARCHAR(12)  NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NULL,
    "order"     INTEGER      NOT NULL,
    created_by  VARCHAR(12)  NULL,
    created_at  TIMESTAMP    NULL,
    updated_by  VARCHAR(12)  NULL,
    updated_at  TIMESTAMP    NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_page_form FOREIGN KEY (form_id)
        REFERENCES form (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_form_page_form_id ON form_page (form_id);

-- Form Question Table
CREATE TABLE IF NOT EXISTS form_question
(
    id               VARCHAR(12)  NOT NULL,
    page_id          VARCHAR(12)  NOT NULL,
    question         TEXT         NOT NULL,
    required         BOOLEAN      NOT NULL DEFAULT FALSE,
    "order"          INTEGER      NOT NULL,
    alias            VARCHAR(50)  NOT NULL,
    input_type       VARCHAR(30)  NOT NULL,
    question_type    VARCHAR(50)  NOT NULL,
    rating_type      VARCHAR(20)  NULL,
    navigation_config JSONB       NULL,
    options          JSONB        NULL,
    scale_count      INTEGER      NULL,
    description      TEXT         NULL,
    created_by       VARCHAR(12)  NULL,
    created_at       TIMESTAMP    NULL,
    updated_by       VARCHAR(12)  NULL,
    updated_at       TIMESTAMP    NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_fqu_input_type CHECK (input_type IN ('TEXT', 'NUMBER', 'EMAIL', 'PHONE', 'DATE', 'TIME', 'DATETIME_LOCAL', 'NONE')),
    CONSTRAINT chk_fqu_question_type CHECK (question_type IN ('SINGLE_LINE_INPUT', 'CHECKBOXES', 'RADIO_BUTTON_GROUP', 'RATING_SCALE', 'YES_NO', 'LONG_TEXT', 'FILE_UPLOAD')),
    CONSTRAINT chk_fqu_rating_type CHECK (rating_type IS NULL OR rating_type IN ('NONE', 'STAR', 'LABEL')),
    CONSTRAINT fk_question_page FOREIGN KEY (page_id)
        REFERENCES form_page (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_form_question_page_id ON form_question (page_id);

-- Form Response Table
CREATE TABLE IF NOT EXISTS form_response
(
    id            VARCHAR(12) NOT NULL,
    form_id       VARCHAR(12) NOT NULL,
    user_id       VARCHAR(12) NULL,
    booking_id    VARCHAR(12) NULL,
    delete_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_by    VARCHAR(12) NULL,
    created_at    TIMESTAMP   NULL,
    updated_by    VARCHAR(12) NULL,
    updated_at    TIMESTAMP   NULL,
    PRIMARY KEY (id),
    CONSTRAINT chk_fre_delete_status CHECK (delete_status IN ('ACTIVE', 'DELETED')),
    CONSTRAINT fk_response_form FOREIGN KEY (form_id)
        REFERENCES form (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_form_response_form_id ON form_response (form_id);
CREATE INDEX idx_form_response_user_id ON form_response (user_id);
CREATE INDEX idx_form_response_booking_id ON form_response (booking_id);

-- Form Response Question Table
CREATE TABLE IF NOT EXISTS form_response_question
(
    id          VARCHAR(12) NOT NULL,
    response_id VARCHAR(12) NOT NULL,
    question_id VARCHAR(12) NOT NULL,
    answer      JSONB       NOT NULL,
    created_by  VARCHAR(12) NULL,
    created_at  TIMESTAMP   NULL,
    updated_by  VARCHAR(12) NULL,
    updated_at  TIMESTAMP   NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_answer_response FOREIGN KEY (response_id)
        REFERENCES form_response (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id)
        REFERENCES form_question (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_form_response_question_response_id ON form_response_question (response_id);
CREATE INDEX idx_form_response_question_question_id ON form_response_question (question_id);
