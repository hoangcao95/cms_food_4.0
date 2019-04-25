
CREATE TABLE image_label
(
	id bigint primary key not null auto_increment,
    label_name nvarchar(200),
    label_id varchar(200),
    created_date date,
    created_by_user bigint references auth_user(id),
    status tinyint
);

create table image_import
(
	id bigint primary key auto_increment not null,
    image_name varchar(200),
    label_id varchar(200) references image_label(label_id),
    path varchar(250),
    created_date date,
    created_by_user bigint references auth_user(id),
    status tinyint
);

CREATE TABLE automl_dataset
(
	id bigint primary key auto_increment not null,
    dataset_name varchar (150) not null,
    dataset_id varchar(200),
    quantity_image int, 
    created_date Date,
    status tinyint
)

CREATE TABLE automl_model
(
	id bigint primary key auto_increment not null,
    model_name varchar (150) not null,
    model_id varchar(200),
    dataset_name varchar(150) references automl_dataset(dataset_name),
    quantity_image int, 
    created_date Date,
    status tinyint
)

