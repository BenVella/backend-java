alter table player_profile
    add column if not exists external_subject varchar(128);

update player_profile
set external_subject = handle
where external_subject is null;

alter table player_profile
    alter column external_subject set not null;

alter table player_profile
    add constraint uq_player_profile_external_subject unique (external_subject);

create table if not exists player_location (
    player_id uuid primary key references player_profile (id) on delete cascade,
    zone_id varchar(64) not null,
    position_x double precision not null,
    position_y double precision not null,
    updated_at timestamptz not null,
    constraint chk_player_location_zone_not_blank check (btrim(zone_id) <> '')
);

create index if not exists ix_player_location_zone_id on player_location (zone_id);
