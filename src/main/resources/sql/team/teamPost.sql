CREATE TABLE TBL_TEAM_POST(
    ID NUMBER PRIMARY KEY,
    TEAM_ID NUMBER,
    END_DATE DATE NOT NULL,
    INFORMATION VARCHAR2(255),
    CREATED_DATE DATE DEFAULT CURRENT_TIMESTAMP,
    UPDATED_DATE DATE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_TEAM_POST_TEAM FOREIGN KEY(TEAM_ID)
    REFERENCES TBL_TEAM(ID)
);

CREATE SEQUENCE SEQ_TEAM_POST;

SELECT * FROM TBL_TEAM_POST;
