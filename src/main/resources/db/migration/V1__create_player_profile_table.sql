create table if not exists player_profile (
    id uuid primary key,
    handle varchar(32) not null,
    display_name varchar(64) not null,
    created_at timestamptz not null,
    updated_at timestamptz not null,
    constraint uq_player_profile_handle unique (handle),
    constraint chk_player_profile_handle_not_blank check (btrim(handle) <> ''),
    constraint chk_player_profile_display_name_not_blank check (btrim(display_name) <> '')
);

create index if not exists ix_player_profile_handle on player_profile (handle);
