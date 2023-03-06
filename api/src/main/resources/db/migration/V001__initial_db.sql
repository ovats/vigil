create extension hstore;

create schema vigil;

CREATE TABLE vigil."User" (
	id bigserial NOT NULL,
	name varchar NOT NULL,
	email varchar NOT NULL,
	CONSTRAINT "pk_user" PRIMARY KEY (id)
);
CREATE UNIQUE INDEX idx_user_email ON vigil."User" USING btree (email);

CREATE TABLE vigil."Post" (
	id bigserial NOT NULL,
	user_id bigserial NOT NULL,
	title varchar NOT NULL,
	created_at varchar NOT NULL,
	CONSTRAINT "pk_post" PRIMARY KEY (id),
	CONSTRAINT "fk_post_user_id" FOREIGN KEY (user_id) REFERENCES vigil."User"(id)
);
CREATE INDEX idx_post_user_id ON vigil."Post" USING btree (user_id);

CREATE TABLE vigil."Comment" (
	id bigserial NOT NULL,
	user_id bigserial NOT NULL,
	post_id bigserial NOT NULL,
	text varchar NOT NULL,
	created_at varchar NOT NULL,
	CONSTRAINT "pk_comment" PRIMARY KEY (id),
	CONSTRAINT "fk_comment_user_id" FOREIGN KEY (user_id) REFERENCES vigil."User"(id),
	CONSTRAINT "fk_comment_post_id" FOREIGN KEY (post_id) REFERENCES vigil."Post"(id)
);
CREATE INDEX idx_comment_post_id ON vigil."Comment" USING btree (post_id);
