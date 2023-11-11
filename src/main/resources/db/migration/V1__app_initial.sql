CREATE SCHEMA IF NOT EXISTS mylfa;

CREATE TABLE IF NOT EXISTS mylfa.user
(
    id            UUID                        NOT NULL,
    email         VARCHAR                     NOT NULL UNIQUE,
    verified      BOOLEAN                     NOT NULL,
    password      VARCHAR                     NOT NULL,
    modify_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    properties    JSONB,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT unique_user__name_key UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS mylfa.currency_type
(
    id            UUID                        NOT NULL,
    name          VARCHAR                     NOT NULL UNIQUE,
    hide          BOOLEAN                     NOT NULL DEFAULT FALSE,
    modify_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id       UUID,
    CONSTRAINT pk_currency_type PRIMARY KEY (id),
    CONSTRAINT unique_currency_type__name_key UNIQUE (name),
    CONSTRAINT fk_currency_type__user_id FOREIGN KEY (user_id) REFERENCES mylfa.user (id)
);

CREATE INDEX IF NOT EXISTS currency_type__user_fkey ON mylfa.currency_type (user_id);

CREATE TABLE IF NOT EXISTS mylfa.income_category
(
    id            UUID                        NOT NULL,
    name          VARCHAR                     NOT NULL UNIQUE,
    hide          BOOLEAN                     NOT NULL,
    parent_id     UUID,
    modify_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id       UUID,
    CONSTRAINT pk_income_category PRIMARY KEY (id),
    CONSTRAINT unique_income_category__name_key UNIQUE (name),
    CONSTRAINT fk_income_category__user_id FOREIGN KEY (user_id) REFERENCES mylfa.user (id)
);

CREATE INDEX IF NOT EXISTS income_category__user_fkey ON mylfa.income_category (user_id);

CREATE TABLE IF NOT EXISTS mylfa.income
(
    id               UUID                        NOT NULL,
    user_id          UUID,
    category_id      UUID                        NOT NULL,
    currency_type_id UUID                        NOT NULL,
    amount           DECIMAL                     NOT NULL,
    modify_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    creation_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description      VARCHAR,
    CONSTRAINT pk_income PRIMARY KEY (id),
    CONSTRAINT fk_income__user_id FOREIGN KEY (user_id) REFERENCES mylfa.user (id),
    CONSTRAINT fk_income__category_id FOREIGN KEY (category_id) REFERENCES mylfa.income_category (id),
    CONSTRAINT fk_income__currency_type_id FOREIGN KEY (currency_type_id) REFERENCES mylfa.currency_type (id)
);

CREATE INDEX IF NOT EXISTS income__user_fkey ON mylfa.income (user_id);
CREATE INDEX IF NOT EXISTS income__category_fkey ON mylfa.income (category_id);
CREATE INDEX IF NOT EXISTS income__currency_type_fkey ON mylfa.income (currency_type_id);

CREATE TABLE IF NOT EXISTS mylfa.expense_category
(
    id            UUID                        NOT NULL,
    name          VARCHAR                     NOT NULL UNIQUE,
    hide          BOOLEAN                     NOT NULL,
    parent_id     UUID,
    modify_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    user_id       UUID,
    CONSTRAINT pk_expense_category PRIMARY KEY (id),
    CONSTRAINT unique_expense_category__name_key UNIQUE (name),
    CONSTRAINT fk_expense_category__user_id FOREIGN KEY (user_id) REFERENCES mylfa.user (id)
);

CREATE INDEX IF NOT EXISTS expense_category__user_fkey ON mylfa.expense_category (user_id);

CREATE TABLE IF NOT EXISTS mylfa.expense
(
    id               UUID                        NOT NULL,
    user_id          UUID,
    category_id      UUID                        NOT NULL,
    currency_type_id UUID                        NOT NULL,
    amount           DECIMAL                     NOT NULL,
    description      VARCHAR,
    modify_date      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    creation_date    TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_expense PRIMARY KEY (id),
    CONSTRAINT fk_expense__user_id FOREIGN KEY (user_id) REFERENCES mylfa.user (id),
    CONSTRAINT fk_expense__category_id FOREIGN KEY (category_id) REFERENCES mylfa.expense_category (id),
    CONSTRAINT fk_expense__currency_type_id FOREIGN KEY (currency_type_id) REFERENCES mylfa.currency_type (id)
);

CREATE INDEX IF NOT EXISTS expense__user_fkey ON mylfa.expense (user_id);
CREATE INDEX IF NOT EXISTS expense__category_fkey ON mylfa.expense (category_id);
CREATE INDEX IF NOT EXISTS expense__currency_type_fkey ON mylfa.expense (currency_type_id);