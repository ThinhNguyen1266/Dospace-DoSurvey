-- Flyway Migration V1: Create form tables for dosurvey
-- Converted from MySQL to PostgreSQL syntax

-- Form Category Table
CREATE TABLE IF NOT EXISTS form_category
(
    foc_id            VARCHAR(12)  NOT NULL,
    foc_tenant_id     VARCHAR(12)  NULL,
    foc_name          VARCHAR(64)  NOT NULL,
    foc_description   VARCHAR(255) NULL,
    foc_status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    foc_delete_status VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    foc_created_by    VARCHAR(12)  NULL,
    foc_created_at    TIMESTAMP    NULL,
    foc_updated_by    VARCHAR(12)  NULL,
    foc_updated_at    TIMESTAMP    NULL,
    PRIMARY KEY (foc_id),
    CONSTRAINT chk_foc_status CHECK (foc_status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_foc_delete_status CHECK (foc_delete_status IN ('ACTIVE', 'DELETED'))
);

CREATE INDEX idx_foc_name ON form_category (foc_name, foc_status, foc_delete_status);
CREATE INDEX idx_foc_tenant_id ON form_category (foc_tenant_id, foc_delete_status);

-- Form Table
CREATE TABLE IF NOT EXISTS form
(
    fom_id               VARCHAR(12)  NOT NULL,
    fom_tenant_id        VARCHAR(12)  NULL,
    fom_owner_id         VARCHAR(12)  NOT NULL,
    fom_title            VARCHAR(255) NOT NULL,
    fom_description      TEXT         NULL,
    fom_public_link      VARCHAR(255) NOT NULL UNIQUE,
    fom_editor_link      VARCHAR(255) NULL UNIQUE,
    fom_qr_url           VARCHAR(255) NULL,
    fom_logo_image       VARCHAR(255) NULL,
    fom_banner_image     VARCHAR(255) NULL,
    fom_primary_color    VARCHAR(7)   NULL,
    fom_background_color VARCHAR(7)   NULL,
    fom_header_font      VARCHAR(100) NULL,
    fom_question_font    VARCHAR(100) NULL,
    fom_text_font        VARCHAR(100) NULL,
    fom_ai_accept        BOOLEAN      NOT NULL DEFAULT FALSE,
    fom_ai_response      TEXT         NULL,
    fom_responder_access VARCHAR(30)  NOT NULL DEFAULT 'RESTRICTED',
    fom_editor_access    VARCHAR(30)  NOT NULL DEFAULT 'RESTRICTED',
    fom_editors_can_share BOOLEAN     NOT NULL DEFAULT FALSE,
    fom_category_id      VARCHAR(12)  NULL,
    fom_status           VARCHAR(20)  NOT NULL DEFAULT 'DRAFT',
    fom_delete_status    VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    fom_close_mode       VARCHAR(20)  NOT NULL DEFAULT 'MANUAL',
    fom_max_response     INTEGER      NULL,
    fom_auto_thanks      BOOLEAN      NOT NULL DEFAULT FALSE,
    fom_valid_from       TIMESTAMP    NULL,
    fom_valid_through    TIMESTAMP    NULL,
    fom_form_for         VARCHAR(20)  NOT NULL DEFAULT 'ALL',
    fom_created_by       VARCHAR(12)  NULL,
    fom_created_at       TIMESTAMP    NULL,
    fom_updated_by       VARCHAR(12)  NULL,
    fom_updated_at       TIMESTAMP    NULL,
    PRIMARY KEY (fom_id),
    CONSTRAINT chk_fom_status CHECK (fom_status IN ('DRAFT', 'PUBLIC', 'PRIVATE', 'CLOSED')),
    CONSTRAINT chk_fom_delete_status CHECK (fom_delete_status IN ('ACTIVE', 'DELETED')),
    CONSTRAINT chk_fom_close_mode CHECK (fom_close_mode IN ('MANUAL', 'AUTO')),
    CONSTRAINT chk_fom_form_for CHECK (fom_form_for IN ('ALL', 'EVENT', 'TICKET', 'SURVEY')),
    CONSTRAINT chk_fom_responder_access CHECK (fom_responder_access IN ('RESTRICTED', 'TENANT', 'ANYONE_WITH_LINK')),
    CONSTRAINT chk_fom_editor_access CHECK (fom_editor_access IN ('RESTRICTED', 'TENANT', 'ANYONE_WITH_LINK')),
    CONSTRAINT fk_form_category FOREIGN KEY (fom_category_id)
        REFERENCES form_category (foc_id) ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE INDEX idx_fom_category_id ON form (fom_category_id);
CREATE INDEX idx_fom_status ON form (fom_status, fom_delete_status);
CREATE INDEX idx_fom_owner_id ON form (fom_owner_id, fom_delete_status);
CREATE INDEX idx_fom_tenant_id ON form (fom_tenant_id, fom_delete_status);

-- Form Collaborator Table
CREATE TABLE IF NOT EXISTS form_collaborator
(
    fco_id         VARCHAR(12)  NOT NULL,
    fco_form_id    VARCHAR(12)  NOT NULL,
    fco_account_id VARCHAR(12)  NULL,
    fco_email      VARCHAR(255) NOT NULL,
    fco_role       VARCHAR(20)  NOT NULL,
    fco_expires_at TIMESTAMP    NULL,
    fco_created_by VARCHAR(12)  NULL,
    fco_created_at TIMESTAMP    NULL,
    fco_updated_by VARCHAR(12)  NULL,
    fco_updated_at TIMESTAMP    NULL,
    PRIMARY KEY (fco_id),
    CONSTRAINT chk_fco_role CHECK (fco_role IN ('EDITOR', 'RESPONDER')),
    CONSTRAINT fk_collaborator_form FOREIGN KEY (fco_form_id)
        REFERENCES form (fom_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_fco_form_id ON form_collaborator (fco_form_id);
CREATE INDEX idx_fco_account_id ON form_collaborator (fco_account_id);

-- Form Page Table
CREATE TABLE IF NOT EXISTS form_page
(
    fpg_id          VARCHAR(12)  NOT NULL,
    fpg_form_id     VARCHAR(12)  NOT NULL,
    fpg_title       VARCHAR(255) NOT NULL,
    fpg_description TEXT         NULL,
    fpg_order       INTEGER      NOT NULL,
    fpg_created_by  VARCHAR(12)  NULL,
    fpg_created_at  TIMESTAMP    NULL,
    fpg_updated_by  VARCHAR(12)  NULL,
    fpg_updated_at  TIMESTAMP    NULL,
    PRIMARY KEY (fpg_id),
    CONSTRAINT fk_page_form FOREIGN KEY (fpg_form_id)
        REFERENCES form (fom_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_fpg_form_id ON form_page (fpg_form_id);

-- Form Question Table
CREATE TABLE IF NOT EXISTS form_question
(
    fqu_id               VARCHAR(12)  NOT NULL,
    fqu_page_id          VARCHAR(12)  NOT NULL,
    fqu_question         TEXT         NOT NULL,
    fqu_required         BOOLEAN      NOT NULL DEFAULT FALSE,
    fqu_order            INTEGER      NOT NULL,
    fqu_alias            VARCHAR(50)  NOT NULL,
    fqu_input_type       VARCHAR(30)  NOT NULL,
    fqu_question_type    VARCHAR(50)  NOT NULL,
    fqu_rating_type      VARCHAR(20)  NULL,
    fqu_navigation_config JSONB       NULL,
    fqu_options          JSONB        NULL,
    fqu_scale_count      INTEGER      NULL,
    fqu_description      TEXT         NULL,
    fqu_created_by       VARCHAR(12)  NULL,
    fqu_created_at       TIMESTAMP    NULL,
    fqu_updated_by       VARCHAR(12)  NULL,
    fqu_updated_at       TIMESTAMP    NULL,
    PRIMARY KEY (fqu_id),
    CONSTRAINT chk_fqu_input_type CHECK (fqu_input_type IN ('TEXT', 'NUMBER', 'EMAIL', 'PHONE', 'DATE', 'TIME', 'DATETIME_LOCAL', 'NONE')),
    CONSTRAINT chk_fqu_question_type CHECK (fqu_question_type IN ('SINGLE_LINE_INPUT', 'CHECKBOXES', 'RADIO_BUTTON_GROUP', 'RATING_SCALE', 'YES_NO', 'LONG_TEXT', 'FILE_UPLOAD')),
    CONSTRAINT chk_fqu_rating_type CHECK (fqu_rating_type IS NULL OR fqu_rating_type IN ('NONE', 'STAR', 'LABEL')),
    CONSTRAINT fk_question_page FOREIGN KEY (fqu_page_id)
        REFERENCES form_page (fpg_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_fqu_page_id ON form_question (fqu_page_id);

-- Form Response Table
CREATE TABLE IF NOT EXISTS form_response
(
    fre_id            VARCHAR(12) NOT NULL,
    fre_form_id       VARCHAR(12) NOT NULL,
    fre_user_id       VARCHAR(12) NULL,
    fre_booking_id    VARCHAR(12) NULL,
    fre_delete_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    fre_created_by    VARCHAR(12) NULL,
    fre_created_at    TIMESTAMP   NULL,
    fre_updated_by    VARCHAR(12) NULL,
    fre_updated_at    TIMESTAMP   NULL,
    PRIMARY KEY (fre_id),
    CONSTRAINT chk_fre_delete_status CHECK (fre_delete_status IN ('ACTIVE', 'DELETED')),
    CONSTRAINT fk_response_form FOREIGN KEY (fre_form_id)
        REFERENCES form (fom_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_fre_form_id ON form_response (fre_form_id);
CREATE INDEX idx_fre_user_id ON form_response (fre_user_id);
CREATE INDEX idx_fre_booking_id ON form_response (fre_booking_id);

-- Form Response Question Table
CREATE TABLE IF NOT EXISTS form_response_question
(
    frq_id          VARCHAR(12) NOT NULL,
    frq_response_id VARCHAR(12) NOT NULL,
    frq_question_id VARCHAR(12) NOT NULL,
    frq_answer      JSONB       NOT NULL,
    frq_created_by  VARCHAR(12) NULL,
    frq_created_at  TIMESTAMP   NULL,
    frq_updated_by  VARCHAR(12) NULL,
    frq_updated_at  TIMESTAMP   NULL,
    PRIMARY KEY (frq_id),
    CONSTRAINT fk_answer_response FOREIGN KEY (frq_response_id)
        REFERENCES form_response (fre_id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_answer_question FOREIGN KEY (frq_question_id)
        REFERENCES form_question (fqu_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE INDEX idx_frq_response_id ON form_response_question (frq_response_id);
CREATE INDEX idx_frq_question_id ON form_response_question (frq_question_id);
