USE warehousesys;
GO

/*create schema business;
create schema sensorsys;*/
create table WarehouseType (
    id int identity(10001, 1) primary key,
    code varchar(20) unique not null,
    temperatureLower decimal (5, 2),
    temperatureUpper decimal (5, 2),
    humidityLower decimal (5, 2),
    humidityUpper decimal (5, 2)
);

create table Warehouse (
    id int identity(10001, 1) primary key,
    code varchar(20) not null unique,
    typeId int not null,
    address varchar(300),
    foreign key (typeId) references WarehouseType(id)
);

create table Container (
    id int identity(10001, 1) primary key,
    warehouseId int not null,
    name varchar(100) not null,
    ordinal int not null,
    ftVolume decimal (10, 2),
    foreign key (warehouseId) references Warehouse(id),
    unique (warehouseId, ordinal)
);

create table Customer (
    id int identity(1000001, 1) primary key,
    fullName varchar(200) not null,
);

create table Rental (
    containerId int not null,
    customerId int not null,
    primary key (containerId, customerId),
    foreign key (containerId) references Container(id),
    foreign key (customerId) references Warehouse(id)
);

-- sensor system tables
create table SensorSystemInfo (
    id int identity(10001, 1) primary key,
    warehouseId int not null unique,
    temperatureSystemSensorTotal int not null,
    humiditySystemSensorTotal int not null,
    sideTemperatureSensorTotal int not null,
    sideHumiditySensorTotal int not null,
    containerStatusSensorEach int not null,
    foreign key (warehouseId) references Warehouse(id)
);

-- sensor comm tables
create table Role (
    name varchar(200) primary key,
    isObserver bit not null,
    isSensor bit not null,
    authName varchar(200) not null unique,
    authPassword varchar(200) not null
);

create table TopicGroup (
    name varchar(200) primary key,
    topicPattern varchar(200) not null,
    recordMaxLength int not null,
    recordPolicy varchar(255) not null
);

create table Permission (
    topicName varchar(200) not null,
    roleName varchar(200) null,
    activity varchar(20) not null,
    foreign key (topicName) references TopicGroup(name),
    foreign key (roleName) references Role(name)
);

create table Observer (
    name varchar(200) primary key,
    password varchar(200) not null,
    roleName varchar(200) not null,
    foreign key (roleName) references Role(name)
);

create table ObserverParam (
    id int identity(10001, 1) primary key,
    observerName varchar(200) not null,
    warehouseCode varchar(20) not null,
    containerOrdinal varchar(20) not null,
    sensorOrdinal varchar(20) not null,
    foreign key (observerName) references Observer(name)
);


--- temporary table for creating data
create table LocationMeta (
      cityCode varchar(20) primary key,
      cityName varchar(20),
      typeWarehouseEach int,
      containerEach int
);


-- table contains param for mqtt connection
create table SensorParam (
        sensorId varchar(255) primary key,
        warehouseCode varchar(100) not null,
        containerOrdinal varchar(100) not null,
        sensorOrdinal varchar(20) not null
)














