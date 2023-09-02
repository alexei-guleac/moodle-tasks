CREATE TABLE access_logs
(
    id          bigserial PRIMARY KEY NOT NULL,
    account_id  bigint,
    description character varying(255),
    name_method character varying(255),
    time      timestamp without time zone,
    type_method integer
);

CREATE TABLE agile_retro_line
(
    id                      bigserial PRIMARY KEY NOT NULL,
    created_at              timestamp without time zone,
    created_by              bigint,
    updated_at              timestamp without time zone,
    updated_by              bigint,
    agile_retro_line_column character varying(255),
    line                    character varying(500),
    user_id                 bigint
);

CREATE TABLE agile_retro_template
(
    id                       bigserial PRIMARY KEY NOT NULL,
    ordinal                  integer,
    retro_template_type      character varying(255),
    retrospective_meeting_id bigint
);

CREATE TABLE agile_retro_template_lines
(
    agile_retro_template_id bigint NOT NULL,
    lines_id                bigint NOT NULL
);

CREATE TABLE app_rating
(
    id         bigserial PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    created_by bigint,
    updated_at timestamp without time zone,
    updated_by bigint,
    rating     integer,
    user_id    bigint
);

CREATE TABLE chat_line
(
    id                       bigserial PRIMARY KEY NOT NULL,
    created_at               timestamp without time zone,
    created_by               bigint,
    updated_at               timestamp without time zone,
    updated_by               bigint,
    line_text                character varying(2400),
    planning_meeting_id      bigint,
    replied_to_id            bigint,
    retrospective_meeting_id bigint,
    standup_meeting_id       bigint,
    user_id                  bigint
);

CREATE TABLE deprecated_retro_template
(
    id                                bigserial PRIMARY KEY NOT NULL,
    created_at                        timestamp without time zone,
    created_by                        bigint,
    updated_at                        timestamp without time zone,
    updated_by                        bigint,
    ordinal                           integer            NOT NULL,
    deprecated_retro_template_type_id bigint
);

CREATE TABLE event_ending_template
(
    id                       bigserial PRIMARY KEY NOT NULL,
    ordinal                  integer,
    retro_template_type      character varying(255),
    retrospective_meeting_id bigint
);

CREATE TABLE event_ending_template_votes
(
    event_ending_template_id bigint NOT NULL,
    votes_id                 bigint NOT NULL
);

CREATE TABLE event_ending_vote
(
    id         bigserial PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    created_by bigint,
    updated_at timestamp without time zone,
    updated_by bigint,
    vote_type  integer,
    user_id    bigint
);

CREATE TABLE feedback
(
    id         bigserial PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    created_by bigint,
    updated_at timestamp without time zone,
    updated_by bigint,
    comment    character varying(1000),
    rating     integer,
    user_id    bigint
);

CREATE TABLE feedback_bundle
(
    id                           bigserial PRIMARY KEY NOT NULL,
    created_at                   timestamp without time zone,
    created_by                   bigint,
    updated_at                   timestamp without time zone,
    updated_by                   bigint,
    all_users_submitted_feedback boolean            NOT NULL,
    meeting_ended_at             timestamp without time zone,
    meeting_id                   bigint,
    meeting_name                 character varying(255),
    meeting_started_at           timestamp without time zone,
    type                         character varying(255),
    project_id                   bigint
);

CREATE TABLE feedback_bundle_feedbacks
(
    feedback_bundle_id bigint NOT NULL,
    feedbacks_id       bigint NOT NULL
);

CREATE TABLE feedback_bundle_users
(
    feedback_bundle_id bigint NOT NULL,
    user_id            bigint NOT NULL
);

CREATE TABLE feedback_bundle_users_seen
(
    feedback_bundle_id bigint NOT NULL,
    user_id            bigint NOT NULL
);

CREATE TABLE feedback_bundle_users_that_should_vote
(
    feedback_bundle_id bigint NOT NULL,
    user_id            bigint NOT NULL
);

CREATE TABLE feedback_result
(
    id                           bigserial PRIMARY KEY NOT NULL,
    created_at                   timestamp without time zone,
    created_by                   bigint,
    updated_at                   timestamp without time zone,
    updated_by                   bigint,
    all_users_submitted_feedback boolean            NOT NULL,
    meeting_ended_at             timestamp without time zone,
    meeting_id                   bigint,
    meeting_name                 character varying(255),
    meeting_started_at           timestamp without time zone,
    type                         character varying(255),
    project_id                   bigint,
    number_of_users_to_vote      integer
);

CREATE TABLE feedback_result_feedbacks
(
    feedback_result_id bigint NOT NULL,
    feedbacks_id       bigint NOT NULL
);

CREATE TABLE feedback_results_users
(
    feedback_result_id bigint NOT NULL,
    user_id            bigint NOT NULL
);

CREATE TABLE feedback_results_users_seen
(
    feedback_result_id bigint NOT NULL,
    user_id            bigint NOT NULL
);

CREATE TABLE feedback_results_users_that_should_vote
(
    feedback_result_id bigint NOT NULL,
    user_id            bigint NOT NULL
);

CREATE TABLE ice_breaking_template
(
    id                       bigserial PRIMARY KEY NOT NULL,
    ordinal                  integer,
    retro_template_type      character varying(255),
    retrospective_meeting_id bigint
);

CREATE TABLE ice_breaking_template_votes
(
    ice_breaking_template_id bigint NOT NULL,
    votes_id                 bigint NOT NULL
);

CREATE TABLE ice_breaking_vote
(
    id                     bigserial PRIMARY KEY NOT NULL,
    created_at             timestamp without time zone,
    created_by             bigint,
    updated_at             timestamp without time zone,
    updated_by             bigint,
    ice_breaking_vote_type integer,
    user_id                bigint
);

CREATE TABLE participant
(
    id         bigserial PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    created_by bigint,
    updated_at timestamp without time zone,
    updated_by bigint,
    email      character varying(255) UNIQUE,
    forename   character varying(255),
    password   character varying(120),
    surname    character varying(255)
);

CREATE TABLE planning
(
    id                    bigserial PRIMARY KEY NOT NULL,
    created_at            timestamp without time zone,
    created_by            bigint,
    updated_at            timestamp without time zone,
    updated_by            bigint,
    archived              boolean            NOT NULL,
    include_notes         boolean            NOT NULL,
    include_timer         boolean            NOT NULL,
    meeting_is_anonymous  boolean,
    link                  character varying(255),
    name                  character varying(255),
    time                time without time zone,
    timer_duration        integer,
    date                  date,
    request_feedback      boolean,
    project_id            bigint,
    last_meeting_ended_at timestamp without time zone
);

CREATE TABLE planning_event
(
    id                      bigserial PRIMARY KEY NOT NULL,
    created_at              timestamp without time zone,
    created_by              bigint,
    updated_at              timestamp without time zone,
    updated_by              bigint,
    end_date                timestamp without time zone,
    is_anonymous            boolean,
    name                    character varying(255),
    start_date              timestamp without time zone,
    active                  boolean            NOT NULL,
    planning_id             bigint,
    project_id              bigint,
    accumulated_break_value integer,
    break_duration          integer,
    break_started_at        timestamp without time zone,
    break_stopped_at        timestamp without time zone,
    break_timer_activated   boolean            NOT NULL,
    last_timer_value        character varying(255),
    timer_duration          integer,
    timer_started_at        timestamp without time zone
);

CREATE TABLE planning_event_user
(
    planning_event_id bigint NOT NULL,
    user_id           bigint NOT NULL
);

CREATE TABLE planning_event_users_that_joined
(
    retrospective_event_id bigint NOT NULL,
    user_id                bigint NOT NULL
);

CREATE TABLE planning_poker
(
    id             bigserial PRIMARY KEY NOT NULL,
    created_at     timestamp without time zone,
    created_by     bigint,
    updated_at     timestamp without time zone,
    updated_by     bigint,
    cards_revealed boolean            NOT NULL,
    planning_id    bigint
);

CREATE TABLE planning_poker_participant
(
    planning_poker_id bigint NOT NULL,
    participant_id    bigint NOT NULL
);

CREATE TABLE planning_user
(
    planning_id bigint NOT NULL,
    user_id     bigint NOT NULL
);

CREATE TABLE poker_participant
(
    id               bigserial PRIMARY KEY NOT NULL,
    created_at       timestamp without time zone,
    created_by       bigint,
    updated_at       timestamp without time zone,
    updated_by       bigint,
    voted            boolean            NOT NULL,
    voted_complexity character varying(255),
    user_id          bigint
);

CREATE TABLE project
(
    id         bigserial PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    created_by bigint,
    updated_at timestamp without time zone,
    updated_by bigint,
    name       character varying(255) UNIQUE
);

CREATE TABLE project_role
(
    id         bigserial PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    created_by bigint,
    updated_at timestamp without time zone,
    updated_by bigint,
    project_id bigint
);

CREATE TABLE project_roles
(
    role_id         bigint,
    project_role_id bigint NOT NULL
);

CREATE TABLE project_roles_user
(
    user_id         bigint NOT NULL,
    project_role_id bigint NOT NULL
);

CREATE TABLE project_user
(
    user_id    bigint NOT NULL,
    project_id bigint NOT NULL
);

CREATE TABLE retro
(
    id                    bigserial PRIMARY KEY NOT NULL,
    created_at            timestamp without time zone,
    created_by            bigint,
    updated_at            timestamp without time zone,
    updated_by            bigint,
    archived              boolean            NOT NULL,
    include_notes         boolean            NOT NULL,
    include_timer         boolean            NOT NULL,
    meeting_is_anonymous  boolean,
    link                  character varying(255),
    name                  character varying(255),
    time                time without time zone,
    timer_duration        integer,
    date                  date,
    discuss_action_points boolean            NOT NULL,
    is_template           boolean            NOT NULL,
    request_feedback      boolean            NOT NULL,
    project_id            bigint,
    last_meeting_ended_at timestamp without time zone
);

CREATE TABLE retro_event
(
    id                      bigserial PRIMARY KEY NOT NULL,
    created_at              timestamp without time zone,
    created_by              bigint,
    updated_at              timestamp without time zone,
    updated_by              bigint,
    end_date                timestamp without time zone,
    is_anonymous            boolean,
    name                    character varying(255),
    start_date              timestamp without time zone,
    active                  boolean            NOT NULL,
    current_ordinal         integer,
    timer_duration          integer,
    timer_paused_at         timestamp without time zone,
    timer_started_at        timestamp without time zone,
    timer_stopped_at        timestamp without time zone,
    project_id              bigint,
    retrospective_id        bigint,
    accumulated_break_value integer,
    break_timer_activated   boolean            NOT NULL,
    break_timer_duration    integer,
    break_timer_started_at  timestamp without time zone,
    break_timer_stopped_at  timestamp without time zone,
    break_duration          integer,
    break_started_at        timestamp without time zone,
    break_stopped_at        timestamp without time zone,
    last_timer_value        character varying(255),
    save_notes              boolean
);

CREATE TABLE retro_template
(
    id                       bigserial PRIMARY KEY NOT NULL,
    ordinal                  integer,
    retro_template_type      character varying(255),
    retrospective_meeting_id bigint
);

CREATE TABLE retro_template_category
(
    id         bigserial PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    created_by bigint,
    updated_at timestamp without time zone,
    updated_by bigint,
    name       character varying(255)
);

CREATE TABLE retro_template_type
(
    id          bigserial PRIMARY KEY NOT NULL,
    created_at  timestamp without time zone,
    created_by  bigint,
    updated_at  timestamp without time zone,
    updated_by  bigint,
    name        character varying(255),
    category_id bigint
);

CREATE TABLE retro_templates
(
    retrospective_id bigint NOT NULL,
    templates_id     bigint NOT NULL
);

CREATE TABLE retro_user
(
    retro_id bigint NOT NULL,
    user_id  bigint NOT NULL
);

CREATE TABLE retrospective_event_user
(
    retrospective_event_id bigint NOT NULL,
    user_id                bigint NOT NULL
);

CREATE TABLE retrospective_event_users_that_joined
(
    retrospective_event_id bigint NOT NULL,
    user_id                bigint NOT NULL
);

CREATE TABLE roles
(
    id         bigserial PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    created_by bigint,
    updated_at timestamp without time zone,
    updated_by bigint,
    name       character varying(20) UNIQUE
);

CREATE TABLE spinning_wheel
(
    id                         bigserial PRIMARY KEY NOT NULL,
    created_at                 timestamp without time zone,
    created_by                 bigint,
    updated_at                 timestamp without time zone,
    updated_by                 bigint,
    accumulated_break_duration integer,
    break_timer_activated      boolean,
    break_timer_duration       integer,
    break_timer_started_at     timestamp without time zone,
    break_timer_stopped_at     timestamp without time zone,
    last_reset_at              timestamp without time zone,
    last_timer_value           character varying(255),
    last_updated_at            timestamp without time zone,
    timer_started_at           timestamp without time zone,
    user_to_speak_id           bigint
);

CREATE TABLE spinning_wheel_participants
(
    spinning_wheel_id bigint NOT NULL,
    user_id           bigint NOT NULL
);

CREATE TABLE spinning_wheel_participants_who_spoke
(
    spinning_wheel_id      bigint NOT NULL,
    standup_participant_id bigint NOT NULL
);

CREATE TABLE spinning_wheel_standup_participants
(
    spinning_wheel_id      bigint NOT NULL,
    standup_participant_id bigint NOT NULL
);

CREATE TABLE standup
(
    id                    bigserial PRIMARY KEY NOT NULL,
    created_at            timestamp without time zone,
    created_by            bigint,
    updated_at            timestamp without time zone,
    updated_by            bigint,
    archived              boolean            NOT NULL,
    include_notes         boolean            NOT NULL,
    include_timer         boolean            NOT NULL,
    meeting_is_anonymous  boolean,
    link                  character varying(255),
    name                  character varying(255),
    time                time without time zone,
    timer_duration        integer,
    project_id            bigint,
    date                  date,
    last_meeting_ended_at timestamp without time zone
);

CREATE TABLE standup_event
(
    id                      bigserial PRIMARY KEY NOT NULL,
    created_at              timestamp without time zone,
    created_by              bigint,
    updated_at              timestamp without time zone,
    updated_by              bigint,
    end_date                timestamp without time zone,
    is_anonymous            boolean,
    name                    character varying(255),
    start_date              timestamp without time zone,
    active                  boolean            NOT NULL,
    timer_started_at        timestamp without time zone,
    project_id              bigint,
    spinning_wheel_id       bigint,
    standup_id              bigint,
    accumulated_break_value integer,
    break_duration          integer,
    break_started_at        timestamp without time zone,
    break_stopped_at        timestamp without time zone,
    last_timer_value        character varying(255),
    timer_duration          integer,
    break_timer_activated   boolean
);

CREATE TABLE standup_event_user
(
    standup_event_id bigint NOT NULL,
    user_id          bigint NOT NULL
);

CREATE TABLE standup_occurrences
(
    standup_id  bigint NOT NULL,
    occurrences character varying(255)
);

CREATE TABLE standup_participant
(
    id         bigserial PRIMARY KEY NOT NULL,
    created_at timestamp without time zone,
    created_by bigint,
    updated_at timestamp without time zone,
    updated_by bigint,
    active     boolean,
    standup_id bigint,
    user_id    bigint,
    guest      boolean
);

CREATE TABLE team_health_check_template
(
    id                       bigserial PRIMARY KEY NOT NULL,
    ordinal                  integer,
    retro_template_type      character varying(255),
    retrospective_meeting_id bigint,
    reveal_results           boolean
);

CREATE TABLE team_health_check_template_criteria
(
    team_health_check_template_id bigint NOT NULL,
    criteria                      character varying(255)
);

CREATE TABLE team_health_check_vote
(
    id             bigserial PRIMARY KEY NOT NULL,
    created_at     timestamp without time zone,
    created_by     bigint,
    updated_at     timestamp without time zone,
    updated_by     bigint,
    criteria       character varying(255),
    current_state  character varying(255),
    state_progress character varying(255)
);

CREATE TABLE team_health_check_vote_bundle
(
    id                            bigserial PRIMARY KEY NOT NULL,
    created_at                    timestamp without time zone,
    created_by                    bigint,
    updated_at                    timestamp without time zone,
    updated_by                    bigint,
    team_health_check_template_id bigint,
    user_id                       bigint
);

CREATE TABLE team_health_check_vote_bundle_votes
(
    team_health_check_vote_bundle_id bigint NOT NULL,
    votes_id                         bigint NOT NULL
);

CREATE TABLE user_meeting_counter
(
    id                         bigserial PRIMARY KEY NOT NULL,
    created_at                 timestamp without time zone,
    created_by                 bigint,
    updated_at                 timestamp without time zone,
    updated_by                 bigint,
    last_meeting_id            bigint,
    meetings_since_last_rating integer            NOT NULL,
    user_id                    bigint
);

CREATE TABLE user_roles
(
    role_id bigint,
    user_id bigint NOT NULL
);


CREATE TABLE hibernate_sequences
(
    sequence_name character varying(255) NOT NULL,
    next_val      bigint
);

CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;