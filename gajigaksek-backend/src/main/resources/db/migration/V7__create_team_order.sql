CREATE TABLE team_order
(
    team_order_id          BIGINT AUTO_INCREMENT NOT NULL,
    created_date           datetime              NULL,
    last_modified_date     datetime              NULL,
    team_id                BIGINT                NOT NULL,
    schedule_id            BIGINT                NOT NULL,
    complete_payment_count INT                   NOT NULL,
    current_payment_count  INT                   NOT NULL,
    team_order_status      VARCHAR(255)          NOT NULL,
    PRIMARY KEY (`team_order_id`)
);