CREATE TABLE Roles_Usuario(
  nombre VARCHAR(11) NOT NULL,
  CONSTRAINT pk_roles_usuario PRIMARY KEY (nombre)
);

CREATE TABLE Usuario(
  alias VARCHAR(111) NOT NULL UNIQUE,
  correo VARCHAR(111) NOT NULL,
  rol VARCHAR(11) NOT NULL,
  contrasenya VARCHAR(555) NOT NULL,
  CONSTRAINT pk_usuario_alias PRIMARY KEY(alias),
  CONSTRAINT fk_usuario_rol FOREIGN KEY (rol)
    REFERENCES roles_usuario(nombre) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE Empresa(
  cif VARCHAR(20) NOT NULL UNIQUE,
  nombre VARCHAR(111) NOT NULL,
  direccion VARCHAR(222) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  CONSTRAINT pk_empresa_alias PRIMARY KEY (cif)
);

CREATE TABLE Persona_de_contacto (
  alias VARCHAR(111) NOT NULL UNIQUE,
  nombre VARCHAR(333) NOT NULL,
  empresa VARCHAR(20) NOT NULL,
  puesto VARCHAR(444) NOT NULL,
  CONSTRAINT fk_persona_de_contacto_alias FOREIGN KEY (alias)
    REFERENCES usuario(alias) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_persona_de_contacto_empresa FOREIGN KEY (empresa)
    REFERENCES empresa(cif) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT pk_persona_de_contacto_alias PRIMARY KEY(alias)
);

CREATE TABLE Tutor (
  alias VARCHAR(111) NOT NULL UNIQUE,
  nombre VARCHAR(333) NOT NULL,
  departamento VARCHAR(222) NOT NULL,
  despacho VARCHAR(444) NOT NULL,
  CONSTRAINT fk_tutor_alias FOREIGN KEY (alias)
    REFERENCES usuario(alias) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT pk_tutor_alias PRIMARY KEY(alias)
);

CREATE TABLE Itinerarios (
  tipo VARCHAR(50) NOT NULL,
  CONSTRAINT pk_itinerarios PRIMARY KEY (tipo)
);

CREATE TABLE Estudiante (
  alias VARCHAR(20) NOT NULL UNIQUE,
  dni VARCHAR(12) NOT NULL UNIQUE,
  nombre VARCHAR(20) NOT NULL,
  apellido VARCHAR(50) NOT NULL,
  telefono INTEGER NOT NULL,
  anyo_academico SMALLINT NOT NULL,
  numero_creditos_aprobados SMALLINT NOT NULL,
  itinerario VARCHAR(50) NOT NULL,
  semestre_elegido SMALLINT NOT NULL,
  CONSTRAINT fk_estudiante_alias FOREIGN KEY (alias)
    REFERENCES usuario(alias) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_estudiante_itinerario FOREIGN KEY (itinerario)
    REFERENCES Itinerarios(tipo) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT pk_estudiante_alias PRIMARY KEY(alias),
  CONSTRAINT ch_estudiante_orden CHECK( 0 < semestre_elegido)
);

CREATE TABLE Estados_de_oferta (
  tipo VARCHAR(30) NOT NULL,
  CONSTRAINT pk_estados_de_oferta PRIMARY KEY (tipo)
);

CREATE TABLE Ofertas_de_practicas (
  id SERIAL NOT NULL,
  titulo VARCHAR(111) NOT NULL,
  descripcion VARCHAR(99999) NOT NULL,
  persona_de_contacto VARCHAR(111) NOT NULL,
  estado VARCHAR(30) NOT NULL,
  fecha_alta DATE NOT NULL,
  fecha_ultima_modificacion DATE NOT NULL,
  pago INTEGER NOT NULL,
  CONSTRAINT fk_oferta_de_practicas_persona_de_contacto
    FOREIGN KEY (persona_de_contacto)
    REFERENCES Persona_de_contacto(alias) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_oferta_de_practicas_estado FOREIGN KEY (estado)
    REFERENCES Estados_de_oferta(tipo) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT pk_oferta_de_practicas_id PRIMARY KEY(id),
  CONSTRAINT ch_oferta_de_practicas_pago CHECK( 0 <= pago )
);

CREATE TABLE Revisiones_de_la_oferta_de_practicas (
  id_oferta INTEGER NOT NULL,
  descripcion VARCHAR(99999) NOT NULL,
  mensaje_de_revision VARCHAR(99999) NOT NULL,
  fecha_solicitud DATE NOT NULL,
  CONSTRAINT fk_revisiones_de_la_oferta_de_practicas FOREIGN KEY (id_oferta)
    REFERENCES Ofertas_de_practicas(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT pk_revisiones_de_la_oferta_de_practicas
    PRIMARY KEY(id_oferta, fecha_solicitud)
);

CREATE TABLE Itinerarios_de_la_oferta_de_practicas (
  id_oferta INTEGER NOT NULL,
  itinerario VARCHAR(99999) NOT NULL,
  CONSTRAINT fk_itinerarios_de_la_oferta_de_practicas
    FOREIGN KEY (id_oferta) REFERENCES Ofertas_de_practicas(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_itinerarios_de_la_oferta_de_practicas_itinerarios
    FOREIGN KEY (itinerario) REFERENCES Itinerarios(tipo)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT pk_itinerarios_de_la_oferta_de_practicas
    PRIMARY KEY(id_oferta, itinerario)
);

CREATE TABLE Estados_preferencia_de_practicas (
  tipo VARCHAR(30) NOT NULL,
  CONSTRAINT pk_estados_preferencia_de_practicas PRIMARY KEY (tipo)
);

CREATE TABLE Preferencia (
  alumno VARCHAR(111) NOT NULL,
  id_oferta INT NOT NULL,
  prioridad SMALLINT NOT NULL,
  estado VARCHAR(30) NOT NULL,
  CONSTRAINT fk_preferencia_alias FOREIGN KEY (alumno)
    REFERENCES Estudiante(alias) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_preferencia_ofertas_practicas FOREIGN KEY (id_oferta)
    REFERENCES Ofertas_de_practicas(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_preferencia_estado FOREIGN KEY (estado)
    REFERENCES Estados_preferencia_de_practicas(tipo)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT pk_preferencia PRIMARY KEY(alumno, id_oferta),
  CONSTRAINT ch_preferencia_prioridad CHECK( 0 < prioridad)
);

CREATE TABLE Estados_de_asignacion (
  tipo VARCHAR(30) NOT NULL,
  CONSTRAINT pk_estados_de_asignacion PRIMARY KEY (tipo)
);

CREATE TABLE Asignacion(
  id_oferta INTEGER NOT NULL,
  alumno VARCHAR(111) NOT NULL,
  tutor VARCHAR(111) NOT NULL,
  estado VARCHAR(40) NOT NULL,
  fecha_de_la_asignacion DATE NOT NULL,
  fecha_de_aceptacion DATE,
  fecha_de_traspaso_iglu DATE,
  fecha_peticion_de_cambio DATE,
  comentario_peticion_de_cambio VARCHAR(99999),
  CONSTRAINT pk_asigna PRIMARY KEY(id_oferta, alumno, tutor),
  CONSTRAINT fk_asigna_id_oferta FOREIGN KEY (id_oferta)
    REFERENCES Ofertas_de_practicas(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_asigna_alumno FOREIGN KEY (alumno)
    REFERENCES Estudiante(alias) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_asigna_tutor FOREIGN KEY (tutor)
    REFERENCES Tutor (alias) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_asigna_estado FOREIGN KEY (estado)
    REFERENCES Estados_de_asignacion (tipo)
    ON DELETE RESTRICT ON UPDATE CASCADE
);
