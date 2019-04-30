--
-- PostgreSQL database dump
--

-- Dumped from database version 10.4 (Debian 10.4-2.pgdg90+1)
-- Dumped by pg_dump version 11.2

-- Started on 2019-04-30 02:55:08 UTC

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 6 (class 2615 OID 312750)
-- Name: configuration; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA configuration;


--
-- TOC entry 8 (class 2615 OID 312751)
-- Name: configuration_audit; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA configuration_audit;


--
-- TOC entry 12 (class 2615 OID 312744)
-- Name: financial; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA financial;


--
-- TOC entry 5 (class 2615 OID 312745)
-- Name: financial_audit; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA financial_audit;


--
-- TOC entry 14 (class 2615 OID 312746)
-- Name: journal; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA journal;


--
-- TOC entry 7 (class 2615 OID 312747)
-- Name: journal_audit; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA journal_audit;


--
-- TOC entry 13 (class 2615 OID 312748)
-- Name: registration; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA registration;


--
-- TOC entry 4 (class 2615 OID 312749)
-- Name: registration_audit; Type: SCHEMA; Schema: -; Owner: -
--

CREATE SCHEMA registration_audit;


SET default_with_oids = false;

--
-- TOC entry 204 (class 1259 OID 312752)
-- Name: authorizations; Type: TABLE; Schema: configuration; Owner: -
--

CREATE TABLE configuration.authorizations (
                                              id bigint NOT NULL,
                                              created_on timestamp without time zone NOT NULL,
                                              updated_on timestamp without time zone,
                                              functionality character varying(90) NOT NULL,
                                              permission character varying(90) NOT NULL
);


--
-- TOC entry 205 (class 1259 OID 312757)
-- Name: configurations; Type: TABLE; Schema: configuration; Owner: -
--

CREATE TABLE configuration.configurations (
                                              id bigint NOT NULL,
                                              created_on timestamp without time zone NOT NULL,
                                              updated_on timestamp without time zone,
                                              id_credit_card_class bigint NOT NULL
);


--
-- TOC entry 206 (class 1259 OID 312762)
-- Name: grants; Type: TABLE; Schema: configuration; Owner: -
--

CREATE TABLE configuration.grants (
                                      id bigint NOT NULL,
                                      created_on timestamp without time zone NOT NULL,
                                      updated_on timestamp without time zone,
                                      id_authorization bigint NOT NULL,
                                      id_group bigint NOT NULL
);


--
-- TOC entry 207 (class 1259 OID 312767)
-- Name: groups; Type: TABLE; Schema: configuration; Owner: -
--

CREATE TABLE configuration.groups (
                                      id bigint NOT NULL,
                                      created_on timestamp without time zone NOT NULL,
                                      updated_on timestamp without time zone,
                                      active boolean NOT NULL,
                                      name character varying(45) NOT NULL,
                                      id_parent bigint
);


--
-- TOC entry 208 (class 1259 OID 312772)
-- Name: profiles; Type: TABLE; Schema: configuration; Owner: -
--

CREATE TABLE configuration.profiles (
                                        id bigint NOT NULL,
                                        created_on timestamp without time zone NOT NULL,
                                        updated_on timestamp without time zone,
                                        active_theme character varying(45) NOT NULL,
                                        show_wallet_balances boolean NOT NULL,
                                        dark_sidebar boolean NOT NULL
);


--
-- TOC entry 209 (class 1259 OID 312777)
-- Name: users; Type: TABLE; Schema: configuration; Owner: -
--

CREATE TABLE configuration.users (
                                     id bigint NOT NULL,
                                     created_on timestamp without time zone NOT NULL,
                                     updated_on timestamp without time zone,
                                     active boolean NOT NULL,
                                     email character varying(90) NOT NULL,
                                     name character varying(90) NOT NULL,
                                     password character varying(60),
                                     store_type character varying(255) NOT NULL,
                                     username character varying(20) NOT NULL,
                                     id_group bigint NOT NULL,
                                     id_profile bigint NOT NULL
);


--
-- TOC entry 210 (class 1259 OID 312787)
-- Name: authorizations; Type: TABLE; Schema: configuration_audit; Owner: -
--

CREATE TABLE configuration_audit.authorizations (
                                                    id bigint NOT NULL,
                                                    revision bigint NOT NULL,
                                                    revision_type smallint,
                                                    functionality character varying(90),
                                                    permission character varying(90)
);


--
-- TOC entry 211 (class 1259 OID 312792)
-- Name: grants; Type: TABLE; Schema: configuration_audit; Owner: -
--

CREATE TABLE configuration_audit.grants (
                                            id bigint NOT NULL,
                                            revision bigint NOT NULL,
                                            revision_type smallint,
                                            id_authorization bigint,
                                            id_group bigint
);


--
-- TOC entry 212 (class 1259 OID 312797)
-- Name: groups; Type: TABLE; Schema: configuration_audit; Owner: -
--

CREATE TABLE configuration_audit.groups (
                                            id bigint NOT NULL,
                                            revision bigint NOT NULL,
                                            revision_type smallint,
                                            active boolean,
                                            name character varying(45),
                                            id_parent bigint
);


--
-- TOC entry 213 (class 1259 OID 312802)
-- Name: profiles; Type: TABLE; Schema: configuration_audit; Owner: -
--

CREATE TABLE configuration_audit.profiles (
                                              id bigint NOT NULL,
                                              revision bigint NOT NULL,
                                              revision_type smallint,
                                              active_theme character varying(45),
                                              show_wallet_balances boolean,
                                              dark_sidebar boolean
);


--
-- TOC entry 214 (class 1259 OID 312807)
-- Name: users; Type: TABLE; Schema: configuration_audit; Owner: -
--

CREATE TABLE configuration_audit.users (
                                           id bigint NOT NULL,
                                           revision bigint NOT NULL,
                                           revision_type smallint,
                                           active boolean,
                                           email character varying(90),
                                           name character varying(90),
                                           password character varying(60),
                                           store_type character varying(255),
                                           username character varying(20),
                                           id_group bigint,
                                           id_profile bigint
);


--
-- TOC entry 215 (class 1259 OID 312815)
-- Name: apportionments; Type: TABLE; Schema: financial; Owner: -
--

CREATE TABLE financial.apportionments (
                                          id bigint NOT NULL,
                                          created_on timestamp without time zone NOT NULL,
                                          updated_on timestamp without time zone,
                                          code character varying(6) NOT NULL,
                                          value numeric(19,2) NOT NULL,
                                          id_cost_center bigint NOT NULL,
                                          id_movement bigint NOT NULL,
                                          id_movement_class bigint NOT NULL
);


--
-- TOC entry 216 (class 1259 OID 312820)
-- Name: closings; Type: TABLE; Schema: financial; Owner: -
--

CREATE TABLE financial.closings (
                                    id bigint NOT NULL,
                                    created_on timestamp without time zone NOT NULL,
                                    updated_on timestamp without time zone,
                                    accumulated numeric(19,2) NOT NULL,
                                    balance numeric(19,2) NOT NULL,
                                    cash_expenses numeric(19,2) NOT NULL,
                                    closing_date date NOT NULL,
                                    credit_card_expenses numeric(19,2) NOT NULL,
                                    debit_card_expenses numeric(19,2) NOT NULL,
                                    expenses numeric(19,2) NOT NULL,
                                    revenues numeric(19,2) NOT NULL,
                                    id_financial_period bigint
);


--
-- TOC entry 217 (class 1259 OID 312825)
-- Name: credit_card_invoices; Type: TABLE; Schema: financial; Owner: -
--

CREATE TABLE financial.credit_card_invoices (
                                                id bigint NOT NULL,
                                                created_on timestamp without time zone NOT NULL,
                                                updated_on timestamp without time zone,
                                                closing_date date,
                                                due_date date NOT NULL,
                                                identification character varying(90) NOT NULL,
                                                invoice_state character varying(45) NOT NULL,
                                                payment_date date,
                                                total_value numeric(19,2) NOT NULL,
                                                id_card bigint,
                                                id_financial_period bigint,
                                                id_period_movement bigint
);


--
-- TOC entry 218 (class 1259 OID 312830)
-- Name: launches; Type: TABLE; Schema: financial; Owner: -
--

CREATE TABLE financial.launches (
                                    id bigint NOT NULL,
                                    created_on timestamp without time zone NOT NULL,
                                    updated_on timestamp without time zone,
                                    code character varying(6) NOT NULL,
                                    quote_number integer,
                                    id_financial_period bigint NOT NULL,
                                    id_fixed_movement bigint NOT NULL,
                                    id_period_movement bigint NOT NULL
);


--
-- TOC entry 219 (class 1259 OID 312835)
-- Name: movements; Type: TABLE; Schema: financial; Owner: -
--

CREATE TABLE financial.movements (
                                     discriminator_value character varying(15) NOT NULL,
                                     id bigint NOT NULL,
                                     created_on timestamp without time zone NOT NULL,
                                     updated_on timestamp without time zone,
                                     code character varying(6) NOT NULL,
                                     description text,
                                     identification character varying(90) NOT NULL,
                                     value numeric(19,2) NOT NULL,
                                     due_date date,
                                     period_movement_state character varying(45),
                                     period_movement_type character varying(45),
                                     actual_quote integer,
                                     auto_launch boolean,
                                     fixed_movement_state character varying(255),
                                     start_date date,
                                     starting_quote integer,
                                     total_quotes integer,
                                     undetermined boolean,
                                     id_contact bigint,
                                     id_credit_card_invoice bigint,
                                     id_financial_period bigint,
                                     id_payment bigint
);


--
-- TOC entry 220 (class 1259 OID 312843)
-- Name: payments; Type: TABLE; Schema: financial; Owner: -
--

CREATE TABLE financial.payments (
                                    id bigint NOT NULL,
                                    created_on timestamp without time zone NOT NULL,
                                    updated_on timestamp without time zone,
                                    discount numeric(19,2),
                                    paid_on date NOT NULL,
                                    paid_value numeric(19,2) NOT NULL,
                                    payment_method character varying(255) NOT NULL,
                                    id_card bigint,
                                    id_wallet bigint
);


--
-- TOC entry 221 (class 1259 OID 312848)
-- Name: transfers; Type: TABLE; Schema: financial; Owner: -
--

CREATE TABLE financial.transfers (
                                     id bigint NOT NULL,
                                     created_on timestamp without time zone NOT NULL,
                                     updated_on timestamp without time zone,
                                     description text,
                                     transfer_date date NOT NULL,
                                     value numeric(19,2) NOT NULL,
                                     id_destination bigint NOT NULL,
                                     id_origin bigint NOT NULL
);


--
-- TOC entry 222 (class 1259 OID 312856)
-- Name: wallet_balances; Type: TABLE; Schema: financial; Owner: -
--

CREATE TABLE financial.wallet_balances (
                                           id bigint NOT NULL,
                                           created_on timestamp without time zone NOT NULL,
                                           updated_on timestamp without time zone,
                                           actual_balance numeric(19,2) NOT NULL,
                                           balance_type character varying(255) NOT NULL,
                                           movement_code character varying(255),
                                           movement_date_time timestamp without time zone NOT NULL,
                                           observations text,
                                           old_balance numeric(19,2) NOT NULL,
                                           reason_type character varying(255) NOT NULL,
                                           transaction_value numeric(19,2) NOT NULL,
                                           id_wallet bigint NOT NULL
);


--
-- TOC entry 223 (class 1259 OID 312945)
-- Name: apportionments; Type: TABLE; Schema: financial_audit; Owner: -
--

CREATE TABLE financial_audit.apportionments (
                                                id bigint NOT NULL,
                                                revision bigint NOT NULL,
                                                revision_type smallint,
                                                code character varying(6),
                                                value numeric(19,2),
                                                id_cost_center bigint,
                                                id_movement bigint,
                                                id_movement_class bigint
);


--
-- TOC entry 224 (class 1259 OID 312950)
-- Name: closings; Type: TABLE; Schema: financial_audit; Owner: -
--

CREATE TABLE financial_audit.closings (
                                          id bigint NOT NULL,
                                          revision bigint NOT NULL,
                                          revision_type smallint,
                                          accumulated numeric(19,2),
                                          balance numeric(19,2),
                                          cash_expenses numeric(19,2),
                                          closing_date date,
                                          credit_card_expenses numeric(19,2),
                                          debit_card_expenses numeric(19,2),
                                          expenses numeric(19,2),
                                          revenues numeric(19,2),
                                          id_financial_period bigint
);


--
-- TOC entry 225 (class 1259 OID 312955)
-- Name: credit_card_invoices; Type: TABLE; Schema: financial_audit; Owner: -
--

CREATE TABLE financial_audit.credit_card_invoices (
                                                      id bigint NOT NULL,
                                                      revision bigint NOT NULL,
                                                      revision_type smallint,
                                                      closing_date date,
                                                      due_date date,
                                                      identification character varying(90),
                                                      invoice_state character varying(45),
                                                      payment_date date,
                                                      total_value numeric(19,2),
                                                      id_card bigint,
                                                      id_financial_period bigint,
                                                      id_period_movement bigint
);


--
-- TOC entry 226 (class 1259 OID 312960)
-- Name: launches; Type: TABLE; Schema: financial_audit; Owner: -
--

CREATE TABLE financial_audit.launches (
                                          id bigint NOT NULL,
                                          revision bigint NOT NULL,
                                          revision_type smallint,
                                          code character varying(6),
                                          quote_number integer,
                                          id_financial_period bigint,
                                          id_fixed_movement bigint,
                                          id_period_movement bigint
);


--
-- TOC entry 227 (class 1259 OID 312965)
-- Name: movements; Type: TABLE; Schema: financial_audit; Owner: -
--

CREATE TABLE financial_audit.movements (
                                           id bigint NOT NULL,
                                           revision bigint NOT NULL,
                                           discriminator_value character varying(15) NOT NULL,
                                           revision_type smallint,
                                           code character varying(6),
                                           description text,
                                           identification character varying(90),
                                           value numeric(19,2),
                                           id_contact bigint,
                                           actual_quote integer,
                                           auto_launch boolean,
                                           fixed_movement_state character varying(255),
                                           start_date date,
                                           starting_quote integer,
                                           total_quotes integer,
                                           undetermined boolean,
                                           due_date date,
                                           period_movement_state character varying(45),
                                           period_movement_type character varying(45),
                                           id_credit_card_invoice bigint,
                                           id_financial_period bigint,
                                           id_payment bigint
);


--
-- TOC entry 228 (class 1259 OID 312973)
-- Name: payments; Type: TABLE; Schema: financial_audit; Owner: -
--

CREATE TABLE financial_audit.payments (
                                          id bigint NOT NULL,
                                          revision bigint NOT NULL,
                                          revision_type smallint,
                                          discount numeric(19,2),
                                          paid_on date,
                                          paid_value numeric(19,2),
                                          payment_method character varying(255),
                                          id_card bigint,
                                          id_wallet bigint
);


--
-- TOC entry 229 (class 1259 OID 312978)
-- Name: transfers; Type: TABLE; Schema: financial_audit; Owner: -
--

CREATE TABLE financial_audit.transfers (
                                           id bigint NOT NULL,
                                           revision bigint NOT NULL,
                                           revision_type smallint,
                                           description text,
                                           transfer_date date,
                                           value numeric(19,2),
                                           id_destination bigint,
                                           id_origin bigint
);


--
-- TOC entry 230 (class 1259 OID 312986)
-- Name: wallet_balances; Type: TABLE; Schema: financial_audit; Owner: -
--

CREATE TABLE financial_audit.wallet_balances (
                                                 id bigint NOT NULL,
                                                 revision bigint NOT NULL,
                                                 revision_type smallint,
                                                 actual_balance numeric(19,2),
                                                 balance_type character varying(255),
                                                 movement_code character varying(255),
                                                 movement_date_time timestamp without time zone,
                                                 observations text,
                                                 old_balance numeric(19,2),
                                                 reason_type character varying(255),
                                                 transaction_value numeric(19,2),
                                                 id_wallet bigint
);


--
-- TOC entry 231 (class 1259 OID 312994)
-- Name: fuels; Type: TABLE; Schema: journal; Owner: -
--

CREATE TABLE journal.fuels (
                               id bigint NOT NULL,
                               created_on timestamp without time zone NOT NULL,
                               updated_on timestamp without time zone,
                               fuel_type character varying(45) NOT NULL,
                               liters numeric(19,2) NOT NULL,
                               value_per_liter numeric(19,2) NOT NULL,
                               id_refueling bigint NOT NULL
);


--
-- TOC entry 232 (class 1259 OID 312999)
-- Name: refuelings; Type: TABLE; Schema: journal; Owner: -
--

CREATE TABLE journal.refuelings (
                                    id bigint NOT NULL,
                                    created_on timestamp without time zone NOT NULL,
                                    updated_on timestamp without time zone,
                                    accounted boolean NOT NULL,
                                    accounted_by character varying(255),
                                    average_consumption numeric(19,2),
                                    code character varying(6),
                                    cost numeric(19,2) NOT NULL,
                                    cost_per_liter numeric(19,2) NOT NULL,
                                    distance bigint NOT NULL,
                                    event_date date NOT NULL,
                                    first_refueling boolean NOT NULL,
                                    full_tank boolean NOT NULL,
                                    liters numeric(19,2) NOT NULL,
                                    odometer bigint NOT NULL,
                                    place character varying(90),
                                    id_financial_period bigint NOT NULL,
                                    id_movement_class bigint NOT NULL,
                                    id_period_movement bigint,
                                    id_vehicle bigint NOT NULL
);


--
-- TOC entry 233 (class 1259 OID 313006)
-- Name: fuels; Type: TABLE; Schema: journal_audit; Owner: -
--

CREATE TABLE journal_audit.fuels (
                                     id bigint NOT NULL,
                                     revision bigint NOT NULL,
                                     revision_type smallint,
                                     fuel_type character varying(45),
                                     liters numeric(19,2),
                                     value_per_liter numeric(19,2),
                                     id_refueling bigint
);


--
-- TOC entry 234 (class 1259 OID 313011)
-- Name: refuelings; Type: TABLE; Schema: journal_audit; Owner: -
--

CREATE TABLE journal_audit.refuelings (
                                          id bigint NOT NULL,
                                          revision bigint NOT NULL,
                                          revision_type smallint,
                                          accounted boolean,
                                          accounted_by character varying(255),
                                          average_consumption numeric(19,2),
                                          code character varying(6),
                                          cost numeric(19,2),
                                          cost_per_liter numeric(19,2),
                                          distance bigint,
                                          event_date date,
                                          first_refueling boolean,
                                          full_tank boolean,
                                          liters numeric(19,2),
                                          odometer bigint,
                                          place character varying(90),
                                          id_financial_period bigint,
                                          id_movement_class bigint,
                                          id_period_movement bigint,
                                          id_vehicle bigint
);


--
-- TOC entry 253 (class 1259 OID 313118)
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 254 (class 1259 OID 313120)
-- Name: pooled_sequence_generator; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.pooled_sequence_generator
    START WITH 1
    INCREMENT BY 5
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 252 (class 1259 OID 313112)
-- Name: revisions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.revisions (
                                  id bigint NOT NULL,
                                  created_by character varying(45) NOT NULL,
                                  created_on timestamp without time zone NOT NULL
);


--
-- TOC entry 251 (class 1259 OID 313110)
-- Name: revisions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.revisions_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 3285 (class 0 OID 0)
-- Dependencies: 251
-- Name: revisions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.revisions_id_seq OWNED BY public.revisions.id;


--
-- TOC entry 235 (class 1259 OID 313016)
-- Name: cards; Type: TABLE; Schema: registration; Owner: -
--

CREATE TABLE registration.cards (
                                    id bigint NOT NULL,
                                    created_on timestamp without time zone NOT NULL,
                                    updated_on timestamp without time zone,
                                    active boolean NOT NULL,
                                    card_type character varying(45) NOT NULL,
                                    credit_limit numeric(19,2),
                                    expiration_day integer,
                                    flag character varying(45) NOT NULL,
                                    name character varying(45) NOT NULL,
                                    number character varying(45) NOT NULL,
                                    owner character varying(45) NOT NULL,
                                    id_wallet bigint
);


--
-- TOC entry 236 (class 1259 OID 313021)
-- Name: contacts; Type: TABLE; Schema: registration; Owner: -
--

CREATE TABLE registration.contacts (
                                       id bigint NOT NULL,
                                       created_on timestamp without time zone NOT NULL,
                                       updated_on timestamp without time zone,
                                       active boolean NOT NULL,
                                       birth_date date,
                                       city character varying(45) NOT NULL,
                                       code character varying(6) NOT NULL,
                                       complement character varying(45),
                                       contact_type character varying(45) NOT NULL,
                                       document character varying(25),
                                       email character varying(90),
                                       name character varying(90) NOT NULL,
                                       neighborhood character varying(45),
                                       number character varying(8),
                                       other_information text,
                                       province character varying(45) NOT NULL,
                                       street character varying(90),
                                       zipcode character varying(9)
);


--
-- TOC entry 237 (class 1259 OID 313029)
-- Name: cost_centers; Type: TABLE; Schema: registration; Owner: -
--

CREATE TABLE registration.cost_centers (
                                           id bigint NOT NULL,
                                           created_on timestamp without time zone NOT NULL,
                                           updated_on timestamp without time zone,
                                           active boolean NOT NULL,
                                           color character varying(21) NOT NULL,
                                           description text,
                                           expenses_budget numeric(19,2) NOT NULL,
                                           name character varying(90) NOT NULL,
                                           revenues_budget numeric(19,2) NOT NULL,
                                           id_parent bigint
);


--
-- TOC entry 238 (class 1259 OID 313037)
-- Name: financial_periods; Type: TABLE; Schema: registration; Owner: -
--

CREATE TABLE registration.financial_periods (
                                                id bigint NOT NULL,
                                                created_on timestamp without time zone NOT NULL,
                                                updated_on timestamp without time zone,
                                                closed boolean NOT NULL,
                                                credit_card_goal numeric(19,2),
                                                end_date date NOT NULL,
                                                expenses_goal numeric(19,2),
                                                expired boolean NOT NULL,
                                                identification character varying(255) NOT NULL,
                                                revenues_goal numeric(19,2),
                                                start_date date NOT NULL
);


--
-- TOC entry 239 (class 1259 OID 313042)
-- Name: movement_classes; Type: TABLE; Schema: registration; Owner: -
--

CREATE TABLE registration.movement_classes (
                                               id bigint NOT NULL,
                                               created_on timestamp without time zone NOT NULL,
                                               updated_on timestamp without time zone,
                                               active boolean NOT NULL,
                                               budget numeric(19,2),
                                               movement_class_type character varying(45) NOT NULL,
                                               name character varying(45) NOT NULL,
                                               id_cost_center bigint NOT NULL
);


--
-- TOC entry 240 (class 1259 OID 313047)
-- Name: telephones; Type: TABLE; Schema: registration; Owner: -
--

CREATE TABLE registration.telephones (
                                         id bigint NOT NULL,
                                         created_on timestamp without time zone NOT NULL,
                                         updated_on timestamp without time zone,
                                         number character varying(20) NOT NULL,
                                         number_type character varying(45) NOT NULL,
                                         id_contact bigint NOT NULL
);


--
-- TOC entry 241 (class 1259 OID 313052)
-- Name: vehicles; Type: TABLE; Schema: registration; Owner: -
--

CREATE TABLE registration.vehicles (
                                       id bigint NOT NULL,
                                       created_on timestamp without time zone NOT NULL,
                                       updated_on timestamp without time zone,
                                       active boolean NOT NULL,
                                       brand character varying(90) NOT NULL,
                                       fuel_capacity integer NOT NULL,
                                       identification character varying(90) NOT NULL,
                                       license_plate character varying(11) NOT NULL,
                                       manufacturing_year integer,
                                       model character varying(90) NOT NULL,
                                       model_year integer,
                                       odometer bigint NOT NULL,
                                       vehicle_type character varying(45) NOT NULL,
                                       id_cost_center bigint NOT NULL
);


--
-- TOC entry 242 (class 1259 OID 313057)
-- Name: wallets; Type: TABLE; Schema: registration; Owner: -
--

CREATE TABLE registration.wallets (
                                      id bigint NOT NULL,
                                      created_on timestamp without time zone NOT NULL,
                                      updated_on timestamp without time zone,
                                      account character varying(45),
                                      active boolean NOT NULL,
                                      actual_balance numeric(19,2) NOT NULL,
                                      agency character varying(10),
                                      bank character varying(45),
                                      description character varying(255),
                                      digit character varying(4),
                                      name character varying(45) NOT NULL,
                                      wallet_type character varying(45) NOT NULL
);


--
-- TOC entry 243 (class 1259 OID 313064)
-- Name: cards; Type: TABLE; Schema: registration_audit; Owner: -
--

CREATE TABLE registration_audit.cards (
                                          id bigint NOT NULL,
                                          revision bigint NOT NULL,
                                          revision_type smallint,
                                          active boolean,
                                          card_type character varying(45),
                                          credit_limit numeric(19,2),
                                          expiration_day integer,
                                          flag character varying(45),
                                          name character varying(45),
                                          number character varying(45),
                                          owner character varying(45),
                                          id_wallet bigint
);


--
-- TOC entry 244 (class 1259 OID 313069)
-- Name: contacts; Type: TABLE; Schema: registration_audit; Owner: -
--

CREATE TABLE registration_audit.contacts (
                                             id bigint NOT NULL,
                                             revision bigint NOT NULL,
                                             revision_type smallint,
                                             active boolean,
                                             birth_date date,
                                             city character varying(45),
                                             code character varying(6),
                                             complement character varying(45),
                                             contact_type character varying(45),
                                             document character varying(25),
                                             email character varying(90),
                                             name character varying(90),
                                             neighborhood character varying(45),
                                             number character varying(8),
                                             other_information text,
                                             province character varying(45),
                                             street character varying(90),
                                             zipcode character varying(9)
);


--
-- TOC entry 245 (class 1259 OID 313077)
-- Name: cost_centers; Type: TABLE; Schema: registration_audit; Owner: -
--

CREATE TABLE registration_audit.cost_centers (
                                                 id bigint NOT NULL,
                                                 revision bigint NOT NULL,
                                                 revision_type smallint,
                                                 active boolean,
                                                 color character varying(21),
                                                 description text,
                                                 expenses_budget numeric(19,2),
                                                 name character varying(90),
                                                 revenues_budget numeric(19,2),
                                                 id_parent bigint
);


--
-- TOC entry 246 (class 1259 OID 313085)
-- Name: financial_periods; Type: TABLE; Schema: registration_audit; Owner: -
--

CREATE TABLE registration_audit.financial_periods (
                                                      id bigint NOT NULL,
                                                      revision bigint NOT NULL,
                                                      revision_type smallint,
                                                      closed boolean,
                                                      credit_card_goal numeric(19,2),
                                                      end_date date,
                                                      expenses_goal numeric(19,2),
                                                      expired boolean,
                                                      identification character varying(255),
                                                      revenues_goal numeric(19,2),
                                                      start_date date
);


--
-- TOC entry 247 (class 1259 OID 313090)
-- Name: movement_classes; Type: TABLE; Schema: registration_audit; Owner: -
--

CREATE TABLE registration_audit.movement_classes (
                                                     id bigint NOT NULL,
                                                     revision bigint NOT NULL,
                                                     revision_type smallint,
                                                     active boolean,
                                                     budget numeric(19,2),
                                                     movement_class_type character varying(45),
                                                     name character varying(45),
                                                     id_cost_center bigint
);


--
-- TOC entry 248 (class 1259 OID 313095)
-- Name: telephones; Type: TABLE; Schema: registration_audit; Owner: -
--

CREATE TABLE registration_audit.telephones (
                                               id bigint NOT NULL,
                                               revision bigint NOT NULL,
                                               revision_type smallint,
                                               number character varying(20),
                                               number_type character varying(45),
                                               id_contact bigint
);


--
-- TOC entry 249 (class 1259 OID 313100)
-- Name: vehicles; Type: TABLE; Schema: registration_audit; Owner: -
--

CREATE TABLE registration_audit.vehicles (
                                             id bigint NOT NULL,
                                             revision bigint NOT NULL,
                                             revision_type smallint,
                                             active boolean,
                                             brand character varying(90),
                                             fuel_capacity integer,
                                             identification character varying(90),
                                             license_plate character varying(11),
                                             manufacturing_year integer,
                                             model character varying(90),
                                             model_year integer,
                                             odometer bigint,
                                             vehicle_type character varying(45),
                                             id_cost_center bigint
);


--
-- TOC entry 250 (class 1259 OID 313105)
-- Name: wallets; Type: TABLE; Schema: registration_audit; Owner: -
--

CREATE TABLE registration_audit.wallets (
                                            id bigint NOT NULL,
                                            revision bigint NOT NULL,
                                            revision_type smallint,
                                            account character varying(45),
                                            active boolean,
                                            actual_balance numeric(19,2),
                                            agency character varying(10),
                                            bank character varying(45),
                                            description character varying(255),
                                            digit character varying(4),
                                            name character varying(45),
                                            wallet_type character varying(45)
);


--
-- TOC entry 2937 (class 2604 OID 313115)
-- Name: revisions id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.revisions ALTER COLUMN id SET DEFAULT nextval('public.revisions_id_seq'::regclass);


--
-- TOC entry 3229 (class 0 OID 312752)
-- Dependencies: 204
-- Data for Name: authorizations; Type: TABLE DATA; Schema: configuration; Owner: -
--

INSERT INTO configuration.authorizations VALUES (16, '2019-04-29 23:51:42.026849', NULL, 'card', 'access');
INSERT INTO configuration.authorizations VALUES (17, '2019-04-29 23:51:42.039844', NULL, 'card', 'add');
INSERT INTO configuration.authorizations VALUES (18, '2019-04-29 23:51:42.048846', NULL, 'card', 'update');
INSERT INTO configuration.authorizations VALUES (19, '2019-04-29 23:51:42.056845', NULL, 'card', 'delete');
INSERT INTO configuration.authorizations VALUES (20, '2019-04-29 23:51:42.064847', NULL, 'card', 'detail');
INSERT INTO configuration.authorizations VALUES (21, '2019-04-29 23:51:42.073846', NULL, 'card', 'statistics');
INSERT INTO configuration.authorizations VALUES (22, '2019-04-29 23:51:42.084845', NULL, 'contact', 'access');
INSERT INTO configuration.authorizations VALUES (23, '2019-04-29 23:51:42.10085', NULL, 'contact', 'add');
INSERT INTO configuration.authorizations VALUES (24, '2019-04-29 23:51:42.109844', NULL, 'contact', 'update');
INSERT INTO configuration.authorizations VALUES (25, '2019-04-29 23:51:42.118844', NULL, 'contact', 'delete');
INSERT INTO configuration.authorizations VALUES (26, '2019-04-29 23:51:42.127844', NULL, 'contact', 'detail');
INSERT INTO configuration.authorizations VALUES (27, '2019-04-29 23:51:42.137845', NULL, 'wallet', 'access');
INSERT INTO configuration.authorizations VALUES (28, '2019-04-29 23:51:42.146844', NULL, 'wallet', 'add');
INSERT INTO configuration.authorizations VALUES (29, '2019-04-29 23:51:42.153845', NULL, 'wallet', 'update');
INSERT INTO configuration.authorizations VALUES (30, '2019-04-29 23:51:42.158844', NULL, 'wallet', 'delete');
INSERT INTO configuration.authorizations VALUES (31, '2019-04-29 23:51:42.164844', NULL, 'wallet', 'detail');
INSERT INTO configuration.authorizations VALUES (32, '2019-04-29 23:51:42.172845', NULL, 'wallet', 'adjust-balance');
INSERT INTO configuration.authorizations VALUES (33, '2019-04-29 23:51:42.179848', NULL, 'cost-center', 'access');
INSERT INTO configuration.authorizations VALUES (34, '2019-04-29 23:51:42.186845', NULL, 'cost-center', 'add');
INSERT INTO configuration.authorizations VALUES (35, '2019-04-29 23:51:42.194845', NULL, 'cost-center', 'update');
INSERT INTO configuration.authorizations VALUES (36, '2019-04-29 23:51:42.202845', NULL, 'cost-center', 'delete');
INSERT INTO configuration.authorizations VALUES (37, '2019-04-29 23:51:42.210844', NULL, 'cost-center', 'detail');
INSERT INTO configuration.authorizations VALUES (38, '2019-04-29 23:51:42.217845', NULL, 'movement-class', 'access');
INSERT INTO configuration.authorizations VALUES (39, '2019-04-29 23:51:42.224844', NULL, 'movement-class', 'add');
INSERT INTO configuration.authorizations VALUES (40, '2019-04-29 23:51:42.233846', NULL, 'movement-class', 'update');
INSERT INTO configuration.authorizations VALUES (41, '2019-04-29 23:51:42.241851', NULL, 'movement-class', 'delete');
INSERT INTO configuration.authorizations VALUES (42, '2019-04-29 23:51:42.252846', NULL, 'movement-class', 'detail');
INSERT INTO configuration.authorizations VALUES (43, '2019-04-29 23:51:42.260845', NULL, 'financial-period', 'access');
INSERT INTO configuration.authorizations VALUES (44, '2019-04-29 23:51:42.268846', NULL, 'financial-period', 'reopen');
INSERT INTO configuration.authorizations VALUES (45, '2019-04-29 23:51:42.277846', NULL, 'financial-period', 'add');
INSERT INTO configuration.authorizations VALUES (46, '2019-04-29 23:51:42.287874', NULL, 'financial-period', 'delete');
INSERT INTO configuration.authorizations VALUES (47, '2019-04-29 23:51:42.295844', NULL, 'financial-period', 'detail');
INSERT INTO configuration.authorizations VALUES (48, '2019-04-29 23:51:42.301847', NULL, 'vehicle', 'access');
INSERT INTO configuration.authorizations VALUES (49, '2019-04-29 23:51:42.308854', NULL, 'vehicle', 'add');
INSERT INTO configuration.authorizations VALUES (50, '2019-04-29 23:51:42.315853', NULL, 'vehicle', 'update');
INSERT INTO configuration.authorizations VALUES (51, '2019-04-29 23:51:42.322845', NULL, 'vehicle', 'delete');
INSERT INTO configuration.authorizations VALUES (52, '2019-04-29 23:51:42.330467', NULL, 'vehicle', 'detail');
INSERT INTO configuration.authorizations VALUES (53, '2019-04-29 23:51:42.335466', NULL, 'refueling', 'access');
INSERT INTO configuration.authorizations VALUES (54, '2019-04-29 23:51:42.341468', NULL, 'refueling', 'add');
INSERT INTO configuration.authorizations VALUES (55, '2019-04-29 23:51:42.346466', NULL, 'refueling', 'delete');
INSERT INTO configuration.authorizations VALUES (56, '2019-04-29 23:51:42.352465', NULL, 'refueling', 'detail');
INSERT INTO configuration.authorizations VALUES (57, '2019-04-29 23:51:42.358466', NULL, 'transference', 'access');
INSERT INTO configuration.authorizations VALUES (58, '2019-04-29 23:51:42.363465', NULL, 'transference', 'transfer');
INSERT INTO configuration.authorizations VALUES (59, '2019-04-29 23:51:42.369466', NULL, 'period-movement', 'access');
INSERT INTO configuration.authorizations VALUES (60, '2019-04-29 23:51:42.374466', NULL, 'period-movement', 'add');
INSERT INTO configuration.authorizations VALUES (61, '2019-04-29 23:51:42.381466', NULL, 'period-movement', 'update');
INSERT INTO configuration.authorizations VALUES (62, '2019-04-29 23:51:42.389474', NULL, 'period-movement', 'detail');
INSERT INTO configuration.authorizations VALUES (63, '2019-04-29 23:51:42.397468', NULL, 'period-movement', 'delete');
INSERT INTO configuration.authorizations VALUES (64, '2019-04-29 23:51:42.405475', NULL, 'period-movement', 'pay');
INSERT INTO configuration.authorizations VALUES (65, '2019-04-29 23:51:42.413467', NULL, 'fixed-movement', 'access');
INSERT INTO configuration.authorizations VALUES (66, '2019-04-29 23:51:42.420478', NULL, 'fixed-movement', 'add');
INSERT INTO configuration.authorizations VALUES (67, '2019-04-29 23:51:42.431464', NULL, 'fixed-movement', 'update');
INSERT INTO configuration.authorizations VALUES (68, '2019-04-29 23:51:42.438466', NULL, 'fixed-movement', 'detail');
INSERT INTO configuration.authorizations VALUES (69, '2019-04-29 23:51:42.446465', NULL, 'fixed-movement', 'delete');
INSERT INTO configuration.authorizations VALUES (70, '2019-04-29 23:51:42.454463', NULL, 'fixed-movement', 'launch');
INSERT INTO configuration.authorizations VALUES (71, '2019-04-29 23:51:42.461469', NULL, 'fixed-movement', 'launches');
INSERT INTO configuration.authorizations VALUES (72, '2019-04-29 23:51:42.471468', NULL, 'credit-card-invoice', 'pay');
INSERT INTO configuration.authorizations VALUES (73, '2019-04-29 23:51:42.479463', NULL, 'credit-card-invoice', 'access');
INSERT INTO configuration.authorizations VALUES (74, '2019-04-29 23:51:42.486463', NULL, 'credit-card-invoice', 'close');
INSERT INTO configuration.authorizations VALUES (75, '2019-04-29 23:51:42.493463', NULL, 'user', 'access');
INSERT INTO configuration.authorizations VALUES (76, '2019-04-29 23:51:42.500463', NULL, 'user', 'add');
INSERT INTO configuration.authorizations VALUES (77, '2019-04-29 23:51:42.50746', NULL, 'user', 'update');
INSERT INTO configuration.authorizations VALUES (78, '2019-04-29 23:51:42.512464', NULL, 'user', 'delete');
INSERT INTO configuration.authorizations VALUES (79, '2019-04-29 23:51:42.517461', NULL, 'user', 'detail');
INSERT INTO configuration.authorizations VALUES (80, '2019-04-29 23:51:42.522466', NULL, 'group', 'access');
INSERT INTO configuration.authorizations VALUES (81, '2019-04-29 23:51:42.527467', NULL, 'group', 'add');
INSERT INTO configuration.authorizations VALUES (82, '2019-04-29 23:51:42.534466', NULL, 'group', 'update');
INSERT INTO configuration.authorizations VALUES (83, '2019-04-29 23:51:42.539465', NULL, 'group', 'delete');
INSERT INTO configuration.authorizations VALUES (84, '2019-04-29 23:51:42.545466', NULL, 'group', 'detail');
INSERT INTO configuration.authorizations VALUES (85, '2019-04-29 23:51:42.551467', NULL, 'configuration', 'access');
INSERT INTO configuration.authorizations VALUES (86, '2019-04-29 23:51:42.556467', NULL, 'closing', 'access');


--
-- TOC entry 3230 (class 0 OID 312757)
-- Dependencies: 205
-- Data for Name: configurations; Type: TABLE DATA; Schema: configuration; Owner: -
--

INSERT INTO configuration.configurations VALUES (11, '2019-04-29 23:51:41.952845', NULL, 6);


--
-- TOC entry 3231 (class 0 OID 312762)
-- Dependencies: 206
-- Data for Name: grants; Type: TABLE DATA; Schema: configuration; Owner: -
--

INSERT INTO configuration.grants VALUES (96, '2019-04-29 23:51:42.727568', NULL, 16, 91);
INSERT INTO configuration.grants VALUES (97, '2019-04-29 23:51:42.729555', NULL, 17, 91);
INSERT INTO configuration.grants VALUES (98, '2019-04-29 23:51:42.729555', NULL, 18, 91);
INSERT INTO configuration.grants VALUES (99, '2019-04-29 23:51:42.73058', NULL, 19, 91);
INSERT INTO configuration.grants VALUES (100, '2019-04-29 23:51:42.73058', NULL, 20, 91);
INSERT INTO configuration.grants VALUES (101, '2019-04-29 23:51:42.73058', NULL, 21, 91);
INSERT INTO configuration.grants VALUES (102, '2019-04-29 23:51:42.732553', NULL, 22, 91);
INSERT INTO configuration.grants VALUES (103, '2019-04-29 23:51:42.732553', NULL, 23, 91);
INSERT INTO configuration.grants VALUES (104, '2019-04-29 23:51:42.732553', NULL, 24, 91);
INSERT INTO configuration.grants VALUES (105, '2019-04-29 23:51:42.733578', NULL, 25, 91);
INSERT INTO configuration.grants VALUES (106, '2019-04-29 23:51:42.733578', NULL, 26, 91);
INSERT INTO configuration.grants VALUES (107, '2019-04-29 23:51:42.735554', NULL, 27, 91);
INSERT INTO configuration.grants VALUES (108, '2019-04-29 23:51:42.735554', NULL, 28, 91);
INSERT INTO configuration.grants VALUES (109, '2019-04-29 23:51:42.735554', NULL, 29, 91);
INSERT INTO configuration.grants VALUES (110, '2019-04-29 23:51:42.735554', NULL, 30, 91);
INSERT INTO configuration.grants VALUES (111, '2019-04-29 23:51:42.735554', NULL, 31, 91);
INSERT INTO configuration.grants VALUES (112, '2019-04-29 23:51:42.738556', NULL, 32, 91);
INSERT INTO configuration.grants VALUES (113, '2019-04-29 23:51:42.738556', NULL, 33, 91);
INSERT INTO configuration.grants VALUES (114, '2019-04-29 23:51:42.738556', NULL, 34, 91);
INSERT INTO configuration.grants VALUES (115, '2019-04-29 23:51:42.738556', NULL, 35, 91);
INSERT INTO configuration.grants VALUES (116, '2019-04-29 23:51:42.738556', NULL, 36, 91);
INSERT INTO configuration.grants VALUES (117, '2019-04-29 23:51:42.740552', NULL, 37, 91);
INSERT INTO configuration.grants VALUES (118, '2019-04-29 23:51:42.741555', NULL, 38, 91);
INSERT INTO configuration.grants VALUES (119, '2019-04-29 23:51:42.741555', NULL, 39, 91);
INSERT INTO configuration.grants VALUES (120, '2019-04-29 23:51:42.741555', NULL, 40, 91);
INSERT INTO configuration.grants VALUES (121, '2019-04-29 23:51:42.741555', NULL, 41, 91);
INSERT INTO configuration.grants VALUES (122, '2019-04-29 23:51:42.744567', NULL, 42, 91);
INSERT INTO configuration.grants VALUES (123, '2019-04-29 23:51:42.744567', NULL, 43, 91);
INSERT INTO configuration.grants VALUES (124, '2019-04-29 23:51:42.744567', NULL, 44, 91);
INSERT INTO configuration.grants VALUES (125, '2019-04-29 23:51:42.744567', NULL, 45, 91);
INSERT INTO configuration.grants VALUES (126, '2019-04-29 23:51:42.745565', NULL, 46, 91);
INSERT INTO configuration.grants VALUES (127, '2019-04-29 23:51:42.747554', NULL, 47, 91);
INSERT INTO configuration.grants VALUES (128, '2019-04-29 23:51:42.747554', NULL, 48, 91);
INSERT INTO configuration.grants VALUES (129, '2019-04-29 23:51:42.747554', NULL, 49, 91);
INSERT INTO configuration.grants VALUES (130, '2019-04-29 23:51:42.748551', NULL, 50, 91);
INSERT INTO configuration.grants VALUES (131, '2019-04-29 23:51:42.748551', NULL, 51, 91);
INSERT INTO configuration.grants VALUES (132, '2019-04-29 23:51:42.750555', NULL, 52, 91);
INSERT INTO configuration.grants VALUES (133, '2019-04-29 23:51:42.750555', NULL, 53, 91);
INSERT INTO configuration.grants VALUES (134, '2019-04-29 23:51:42.751552', NULL, 54, 91);
INSERT INTO configuration.grants VALUES (135, '2019-04-29 23:51:42.751552', NULL, 55, 91);
INSERT INTO configuration.grants VALUES (136, '2019-04-29 23:51:42.751552', NULL, 56, 91);
INSERT INTO configuration.grants VALUES (137, '2019-04-29 23:51:42.753553', NULL, 57, 91);
INSERT INTO configuration.grants VALUES (138, '2019-04-29 23:51:42.753553', NULL, 58, 91);
INSERT INTO configuration.grants VALUES (139, '2019-04-29 23:51:42.754552', NULL, 59, 91);
INSERT INTO configuration.grants VALUES (140, '2019-04-29 23:51:42.754552', NULL, 60, 91);
INSERT INTO configuration.grants VALUES (141, '2019-04-29 23:51:42.754552', NULL, 61, 91);
INSERT INTO configuration.grants VALUES (142, '2019-04-29 23:51:42.756552', NULL, 62, 91);
INSERT INTO configuration.grants VALUES (143, '2019-04-29 23:51:42.756552', NULL, 63, 91);
INSERT INTO configuration.grants VALUES (144, '2019-04-29 23:51:42.756552', NULL, 64, 91);
INSERT INTO configuration.grants VALUES (145, '2019-04-29 23:51:42.757559', NULL, 65, 91);
INSERT INTO configuration.grants VALUES (146, '2019-04-29 23:51:42.757559', NULL, 66, 91);
INSERT INTO configuration.grants VALUES (147, '2019-04-29 23:51:42.759552', NULL, 67, 91);
INSERT INTO configuration.grants VALUES (148, '2019-04-29 23:51:42.759552', NULL, 68, 91);
INSERT INTO configuration.grants VALUES (149, '2019-04-29 23:51:42.759552', NULL, 69, 91);
INSERT INTO configuration.grants VALUES (150, '2019-04-29 23:51:42.760552', NULL, 70, 91);
INSERT INTO configuration.grants VALUES (151, '2019-04-29 23:51:42.760552', NULL, 71, 91);
INSERT INTO configuration.grants VALUES (152, '2019-04-29 23:51:42.762559', NULL, 72, 91);
INSERT INTO configuration.grants VALUES (153, '2019-04-29 23:51:42.762559', NULL, 73, 91);
INSERT INTO configuration.grants VALUES (154, '2019-04-29 23:51:42.762559', NULL, 74, 91);
INSERT INTO configuration.grants VALUES (155, '2019-04-29 23:51:42.763552', NULL, 75, 91);
INSERT INTO configuration.grants VALUES (156, '2019-04-29 23:51:42.763552', NULL, 76, 91);
INSERT INTO configuration.grants VALUES (157, '2019-04-29 23:51:42.765552', NULL, 77, 91);
INSERT INTO configuration.grants VALUES (158, '2019-04-29 23:51:42.765552', NULL, 78, 91);
INSERT INTO configuration.grants VALUES (159, '2019-04-29 23:51:42.765552', NULL, 79, 91);
INSERT INTO configuration.grants VALUES (160, '2019-04-29 23:51:42.765552', NULL, 80, 91);
INSERT INTO configuration.grants VALUES (161, '2019-04-29 23:51:42.766553', NULL, 81, 91);
INSERT INTO configuration.grants VALUES (162, '2019-04-29 23:51:42.767553', NULL, 82, 91);
INSERT INTO configuration.grants VALUES (163, '2019-04-29 23:51:42.768552', NULL, 83, 91);
INSERT INTO configuration.grants VALUES (164, '2019-04-29 23:51:42.768552', NULL, 84, 91);
INSERT INTO configuration.grants VALUES (165, '2019-04-29 23:51:42.768552', NULL, 85, 91);
INSERT INTO configuration.grants VALUES (166, '2019-04-29 23:51:42.768552', NULL, 86, 91);


--
-- TOC entry 3232 (class 0 OID 312767)
-- Dependencies: 207
-- Data for Name: groups; Type: TABLE DATA; Schema: configuration; Owner: -
--

INSERT INTO configuration.groups VALUES (91, '2019-04-29 23:51:42.709553', NULL, true, 'Administradores', NULL);


--
-- TOC entry 3233 (class 0 OID 312772)
-- Dependencies: 208
-- Data for Name: profiles; Type: TABLE DATA; Schema: configuration; Owner: -
--

INSERT INTO configuration.profiles VALUES (176, '2019-04-29 23:51:43.287313', NULL, 'BLACK', true, true);


--
-- TOC entry 3234 (class 0 OID 312777)
-- Dependencies: 209
-- Data for Name: users; Type: TABLE DATA; Schema: configuration; Owner: -
--

INSERT INTO configuration.users VALUES (171, '2019-04-29 23:51:43.28531', NULL, true, 'contato@webbudget.com.br', 'Administrador', '$2a$10$KMIRSTgZxCEQR4anDj7rX.YnBXJkZJwJBXlygDj5ElhECf3iDQ.kS', 'LOCAL', 'admin', 91, 176);


--
-- TOC entry 3235 (class 0 OID 312787)
-- Dependencies: 210
-- Data for Name: authorizations; Type: TABLE DATA; Schema: configuration_audit; Owner: -
--

INSERT INTO configuration_audit.authorizations VALUES (16, 2, 0, 'card', 'access');
INSERT INTO configuration_audit.authorizations VALUES (17, 2, 0, 'card', 'add');
INSERT INTO configuration_audit.authorizations VALUES (18, 2, 0, 'card', 'update');
INSERT INTO configuration_audit.authorizations VALUES (19, 2, 0, 'card', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (20, 2, 0, 'card', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (21, 2, 0, 'card', 'statistics');
INSERT INTO configuration_audit.authorizations VALUES (22, 2, 0, 'contact', 'access');
INSERT INTO configuration_audit.authorizations VALUES (23, 2, 0, 'contact', 'add');
INSERT INTO configuration_audit.authorizations VALUES (24, 2, 0, 'contact', 'update');
INSERT INTO configuration_audit.authorizations VALUES (25, 2, 0, 'contact', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (26, 2, 0, 'contact', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (27, 2, 0, 'wallet', 'access');
INSERT INTO configuration_audit.authorizations VALUES (28, 2, 0, 'wallet', 'add');
INSERT INTO configuration_audit.authorizations VALUES (29, 2, 0, 'wallet', 'update');
INSERT INTO configuration_audit.authorizations VALUES (30, 2, 0, 'wallet', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (31, 2, 0, 'wallet', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (32, 2, 0, 'wallet', 'adjust-balance');
INSERT INTO configuration_audit.authorizations VALUES (33, 2, 0, 'cost-center', 'access');
INSERT INTO configuration_audit.authorizations VALUES (34, 2, 0, 'cost-center', 'add');
INSERT INTO configuration_audit.authorizations VALUES (35, 2, 0, 'cost-center', 'update');
INSERT INTO configuration_audit.authorizations VALUES (36, 2, 0, 'cost-center', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (37, 2, 0, 'cost-center', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (38, 2, 0, 'movement-class', 'access');
INSERT INTO configuration_audit.authorizations VALUES (39, 2, 0, 'movement-class', 'add');
INSERT INTO configuration_audit.authorizations VALUES (40, 2, 0, 'movement-class', 'update');
INSERT INTO configuration_audit.authorizations VALUES (41, 2, 0, 'movement-class', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (42, 2, 0, 'movement-class', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (43, 2, 0, 'financial-period', 'access');
INSERT INTO configuration_audit.authorizations VALUES (44, 2, 0, 'financial-period', 'reopen');
INSERT INTO configuration_audit.authorizations VALUES (45, 2, 0, 'financial-period', 'add');
INSERT INTO configuration_audit.authorizations VALUES (46, 2, 0, 'financial-period', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (47, 2, 0, 'financial-period', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (48, 2, 0, 'vehicle', 'access');
INSERT INTO configuration_audit.authorizations VALUES (49, 2, 0, 'vehicle', 'add');
INSERT INTO configuration_audit.authorizations VALUES (50, 2, 0, 'vehicle', 'update');
INSERT INTO configuration_audit.authorizations VALUES (51, 2, 0, 'vehicle', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (52, 2, 0, 'vehicle', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (53, 2, 0, 'refueling', 'access');
INSERT INTO configuration_audit.authorizations VALUES (54, 2, 0, 'refueling', 'add');
INSERT INTO configuration_audit.authorizations VALUES (55, 2, 0, 'refueling', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (56, 2, 0, 'refueling', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (57, 2, 0, 'transference', 'access');
INSERT INTO configuration_audit.authorizations VALUES (58, 2, 0, 'transference', 'transfer');
INSERT INTO configuration_audit.authorizations VALUES (59, 2, 0, 'period-movement', 'access');
INSERT INTO configuration_audit.authorizations VALUES (60, 2, 0, 'period-movement', 'add');
INSERT INTO configuration_audit.authorizations VALUES (61, 2, 0, 'period-movement', 'update');
INSERT INTO configuration_audit.authorizations VALUES (62, 2, 0, 'period-movement', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (63, 2, 0, 'period-movement', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (64, 2, 0, 'period-movement', 'pay');
INSERT INTO configuration_audit.authorizations VALUES (65, 2, 0, 'fixed-movement', 'access');
INSERT INTO configuration_audit.authorizations VALUES (66, 2, 0, 'fixed-movement', 'add');
INSERT INTO configuration_audit.authorizations VALUES (67, 2, 0, 'fixed-movement', 'update');
INSERT INTO configuration_audit.authorizations VALUES (68, 2, 0, 'fixed-movement', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (69, 2, 0, 'fixed-movement', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (70, 2, 0, 'fixed-movement', 'launch');
INSERT INTO configuration_audit.authorizations VALUES (71, 2, 0, 'fixed-movement', 'launches');
INSERT INTO configuration_audit.authorizations VALUES (72, 2, 0, 'credit-card-invoice', 'pay');
INSERT INTO configuration_audit.authorizations VALUES (73, 2, 0, 'credit-card-invoice', 'access');
INSERT INTO configuration_audit.authorizations VALUES (74, 2, 0, 'credit-card-invoice', 'close');
INSERT INTO configuration_audit.authorizations VALUES (75, 2, 0, 'user', 'access');
INSERT INTO configuration_audit.authorizations VALUES (76, 2, 0, 'user', 'add');
INSERT INTO configuration_audit.authorizations VALUES (77, 2, 0, 'user', 'update');
INSERT INTO configuration_audit.authorizations VALUES (78, 2, 0, 'user', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (79, 2, 0, 'user', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (80, 2, 0, 'group', 'access');
INSERT INTO configuration_audit.authorizations VALUES (81, 2, 0, 'group', 'add');
INSERT INTO configuration_audit.authorizations VALUES (82, 2, 0, 'group', 'update');
INSERT INTO configuration_audit.authorizations VALUES (83, 2, 0, 'group', 'delete');
INSERT INTO configuration_audit.authorizations VALUES (84, 2, 0, 'group', 'detail');
INSERT INTO configuration_audit.authorizations VALUES (85, 2, 0, 'configuration', 'access');
INSERT INTO configuration_audit.authorizations VALUES (86, 2, 0, 'closing', 'access');


--
-- TOC entry 3236 (class 0 OID 312792)
-- Dependencies: 211
-- Data for Name: grants; Type: TABLE DATA; Schema: configuration_audit; Owner: -
--

INSERT INTO configuration_audit.grants VALUES (96, 3, 0, 16, 91);
INSERT INTO configuration_audit.grants VALUES (97, 3, 0, 17, 91);
INSERT INTO configuration_audit.grants VALUES (98, 3, 0, 18, 91);
INSERT INTO configuration_audit.grants VALUES (99, 3, 0, 19, 91);
INSERT INTO configuration_audit.grants VALUES (100, 3, 0, 20, 91);
INSERT INTO configuration_audit.grants VALUES (101, 3, 0, 21, 91);
INSERT INTO configuration_audit.grants VALUES (102, 3, 0, 22, 91);
INSERT INTO configuration_audit.grants VALUES (103, 3, 0, 23, 91);
INSERT INTO configuration_audit.grants VALUES (104, 3, 0, 24, 91);
INSERT INTO configuration_audit.grants VALUES (105, 3, 0, 25, 91);
INSERT INTO configuration_audit.grants VALUES (106, 3, 0, 26, 91);
INSERT INTO configuration_audit.grants VALUES (107, 3, 0, 27, 91);
INSERT INTO configuration_audit.grants VALUES (108, 3, 0, 28, 91);
INSERT INTO configuration_audit.grants VALUES (109, 3, 0, 29, 91);
INSERT INTO configuration_audit.grants VALUES (110, 3, 0, 30, 91);
INSERT INTO configuration_audit.grants VALUES (111, 3, 0, 31, 91);
INSERT INTO configuration_audit.grants VALUES (112, 3, 0, 32, 91);
INSERT INTO configuration_audit.grants VALUES (113, 3, 0, 33, 91);
INSERT INTO configuration_audit.grants VALUES (114, 3, 0, 34, 91);
INSERT INTO configuration_audit.grants VALUES (115, 3, 0, 35, 91);
INSERT INTO configuration_audit.grants VALUES (116, 3, 0, 36, 91);
INSERT INTO configuration_audit.grants VALUES (117, 3, 0, 37, 91);
INSERT INTO configuration_audit.grants VALUES (118, 3, 0, 38, 91);
INSERT INTO configuration_audit.grants VALUES (119, 3, 0, 39, 91);
INSERT INTO configuration_audit.grants VALUES (120, 3, 0, 40, 91);
INSERT INTO configuration_audit.grants VALUES (121, 3, 0, 41, 91);
INSERT INTO configuration_audit.grants VALUES (122, 3, 0, 42, 91);
INSERT INTO configuration_audit.grants VALUES (123, 3, 0, 43, 91);
INSERT INTO configuration_audit.grants VALUES (124, 3, 0, 44, 91);
INSERT INTO configuration_audit.grants VALUES (125, 3, 0, 45, 91);
INSERT INTO configuration_audit.grants VALUES (126, 3, 0, 46, 91);
INSERT INTO configuration_audit.grants VALUES (127, 3, 0, 47, 91);
INSERT INTO configuration_audit.grants VALUES (128, 3, 0, 48, 91);
INSERT INTO configuration_audit.grants VALUES (129, 3, 0, 49, 91);
INSERT INTO configuration_audit.grants VALUES (130, 3, 0, 50, 91);
INSERT INTO configuration_audit.grants VALUES (131, 3, 0, 51, 91);
INSERT INTO configuration_audit.grants VALUES (132, 3, 0, 52, 91);
INSERT INTO configuration_audit.grants VALUES (133, 3, 0, 53, 91);
INSERT INTO configuration_audit.grants VALUES (134, 3, 0, 54, 91);
INSERT INTO configuration_audit.grants VALUES (135, 3, 0, 55, 91);
INSERT INTO configuration_audit.grants VALUES (136, 3, 0, 56, 91);
INSERT INTO configuration_audit.grants VALUES (137, 3, 0, 57, 91);
INSERT INTO configuration_audit.grants VALUES (138, 3, 0, 58, 91);
INSERT INTO configuration_audit.grants VALUES (139, 3, 0, 59, 91);
INSERT INTO configuration_audit.grants VALUES (140, 3, 0, 60, 91);
INSERT INTO configuration_audit.grants VALUES (141, 3, 0, 61, 91);
INSERT INTO configuration_audit.grants VALUES (142, 3, 0, 62, 91);
INSERT INTO configuration_audit.grants VALUES (143, 3, 0, 63, 91);
INSERT INTO configuration_audit.grants VALUES (144, 3, 0, 64, 91);
INSERT INTO configuration_audit.grants VALUES (145, 3, 0, 65, 91);
INSERT INTO configuration_audit.grants VALUES (146, 3, 0, 66, 91);
INSERT INTO configuration_audit.grants VALUES (147, 3, 0, 67, 91);
INSERT INTO configuration_audit.grants VALUES (148, 3, 0, 68, 91);
INSERT INTO configuration_audit.grants VALUES (149, 3, 0, 69, 91);
INSERT INTO configuration_audit.grants VALUES (150, 3, 0, 70, 91);
INSERT INTO configuration_audit.grants VALUES (151, 3, 0, 71, 91);
INSERT INTO configuration_audit.grants VALUES (152, 3, 0, 72, 91);
INSERT INTO configuration_audit.grants VALUES (153, 3, 0, 73, 91);
INSERT INTO configuration_audit.grants VALUES (154, 3, 0, 74, 91);
INSERT INTO configuration_audit.grants VALUES (155, 3, 0, 75, 91);
INSERT INTO configuration_audit.grants VALUES (156, 3, 0, 76, 91);
INSERT INTO configuration_audit.grants VALUES (157, 3, 0, 77, 91);
INSERT INTO configuration_audit.grants VALUES (158, 3, 0, 78, 91);
INSERT INTO configuration_audit.grants VALUES (159, 3, 0, 79, 91);
INSERT INTO configuration_audit.grants VALUES (160, 3, 0, 80, 91);
INSERT INTO configuration_audit.grants VALUES (161, 3, 0, 81, 91);
INSERT INTO configuration_audit.grants VALUES (162, 3, 0, 82, 91);
INSERT INTO configuration_audit.grants VALUES (163, 3, 0, 83, 91);
INSERT INTO configuration_audit.grants VALUES (164, 3, 0, 84, 91);
INSERT INTO configuration_audit.grants VALUES (165, 3, 0, 85, 91);
INSERT INTO configuration_audit.grants VALUES (166, 3, 0, 86, 91);


--
-- TOC entry 3237 (class 0 OID 312797)
-- Dependencies: 212
-- Data for Name: groups; Type: TABLE DATA; Schema: configuration_audit; Owner: -
--

INSERT INTO configuration_audit.groups VALUES (91, 3, 0, true, 'Administradores', NULL);


--
-- TOC entry 3238 (class 0 OID 312802)
-- Dependencies: 213
-- Data for Name: profiles; Type: TABLE DATA; Schema: configuration_audit; Owner: -
--

INSERT INTO configuration_audit.profiles VALUES (176, 4, 0, 'BLACK', true, true);


--
-- TOC entry 3239 (class 0 OID 312807)
-- Dependencies: 214
-- Data for Name: users; Type: TABLE DATA; Schema: configuration_audit; Owner: -
--

INSERT INTO configuration_audit.users VALUES (171, 4, 0, true, 'contato@webbudget.com.br', 'Administrador', '$2a$10$KMIRSTgZxCEQR4anDj7rX.YnBXJkZJwJBXlygDj5ElhECf3iDQ.kS', 'LOCAL', 'admin', 91, 176);


--
-- TOC entry 3240 (class 0 OID 312815)
-- Dependencies: 215
-- Data for Name: apportionments; Type: TABLE DATA; Schema: financial; Owner: -
--



--
-- TOC entry 3241 (class 0 OID 312820)
-- Dependencies: 216
-- Data for Name: closings; Type: TABLE DATA; Schema: financial; Owner: -
--



--
-- TOC entry 3242 (class 0 OID 312825)
-- Dependencies: 217
-- Data for Name: credit_card_invoices; Type: TABLE DATA; Schema: financial; Owner: -
--



--
-- TOC entry 3243 (class 0 OID 312830)
-- Dependencies: 218
-- Data for Name: launches; Type: TABLE DATA; Schema: financial; Owner: -
--



--
-- TOC entry 3244 (class 0 OID 312835)
-- Dependencies: 219
-- Data for Name: movements; Type: TABLE DATA; Schema: financial; Owner: -
--



--
-- TOC entry 3245 (class 0 OID 312843)
-- Dependencies: 220
-- Data for Name: payments; Type: TABLE DATA; Schema: financial; Owner: -
--



--
-- TOC entry 3246 (class 0 OID 312848)
-- Dependencies: 221
-- Data for Name: transfers; Type: TABLE DATA; Schema: financial; Owner: -
--



--
-- TOC entry 3247 (class 0 OID 312856)
-- Dependencies: 222
-- Data for Name: wallet_balances; Type: TABLE DATA; Schema: financial; Owner: -
--



--
-- TOC entry 3248 (class 0 OID 312945)
-- Dependencies: 223
-- Data for Name: apportionments; Type: TABLE DATA; Schema: financial_audit; Owner: -
--



--
-- TOC entry 3249 (class 0 OID 312950)
-- Dependencies: 224
-- Data for Name: closings; Type: TABLE DATA; Schema: financial_audit; Owner: -
--



--
-- TOC entry 3250 (class 0 OID 312955)
-- Dependencies: 225
-- Data for Name: credit_card_invoices; Type: TABLE DATA; Schema: financial_audit; Owner: -
--



--
-- TOC entry 3251 (class 0 OID 312960)
-- Dependencies: 226
-- Data for Name: launches; Type: TABLE DATA; Schema: financial_audit; Owner: -
--



--
-- TOC entry 3252 (class 0 OID 312965)
-- Dependencies: 227
-- Data for Name: movements; Type: TABLE DATA; Schema: financial_audit; Owner: -
--



--
-- TOC entry 3253 (class 0 OID 312973)
-- Dependencies: 228
-- Data for Name: payments; Type: TABLE DATA; Schema: financial_audit; Owner: -
--



--
-- TOC entry 3254 (class 0 OID 312978)
-- Dependencies: 229
-- Data for Name: transfers; Type: TABLE DATA; Schema: financial_audit; Owner: -
--



--
-- TOC entry 3255 (class 0 OID 312986)
-- Dependencies: 230
-- Data for Name: wallet_balances; Type: TABLE DATA; Schema: financial_audit; Owner: -
--



--
-- TOC entry 3256 (class 0 OID 312994)
-- Dependencies: 231
-- Data for Name: fuels; Type: TABLE DATA; Schema: journal; Owner: -
--



--
-- TOC entry 3257 (class 0 OID 312999)
-- Dependencies: 232
-- Data for Name: refuelings; Type: TABLE DATA; Schema: journal; Owner: -
--



--
-- TOC entry 3258 (class 0 OID 313006)
-- Dependencies: 233
-- Data for Name: fuels; Type: TABLE DATA; Schema: journal_audit; Owner: -
--



--
-- TOC entry 3259 (class 0 OID 313011)
-- Dependencies: 234
-- Data for Name: refuelings; Type: TABLE DATA; Schema: journal_audit; Owner: -
--



--
-- TOC entry 3277 (class 0 OID 313112)
-- Dependencies: 252
-- Data for Name: revisions; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO public.revisions VALUES (1, 'unknown', '2019-04-29 23:51:41.959');
INSERT INTO public.revisions VALUES (2, 'unknown', '2019-04-29 23:51:42.561');
INSERT INTO public.revisions VALUES (3, 'unknown', '2019-04-29 23:51:43.056');
INSERT INTO public.revisions VALUES (4, 'unknown', '2019-04-29 23:51:43.298');


--
-- TOC entry 3260 (class 0 OID 313016)
-- Dependencies: 235
-- Data for Name: cards; Type: TABLE DATA; Schema: registration; Owner: -
--



--
-- TOC entry 3261 (class 0 OID 313021)
-- Dependencies: 236
-- Data for Name: contacts; Type: TABLE DATA; Schema: registration; Owner: -
--



--
-- TOC entry 3262 (class 0 OID 313029)
-- Dependencies: 237
-- Data for Name: cost_centers; Type: TABLE DATA; Schema: registration; Owner: -
--

INSERT INTO registration.cost_centers VALUES (1, '2019-04-29 23:51:41.862857', '2019-04-29 23:51:43.30131', true, 'rgba(222,88,188,1.0)', NULL, 0.00, 'Cartão de Crédito', 0.00, NULL);


--
-- TOC entry 3263 (class 0 OID 313037)
-- Dependencies: 238
-- Data for Name: financial_periods; Type: TABLE DATA; Schema: registration; Owner: -
--



--
-- TOC entry 3264 (class 0 OID 313042)
-- Dependencies: 239
-- Data for Name: movement_classes; Type: TABLE DATA; Schema: registration; Owner: -
--

INSERT INTO registration.movement_classes VALUES (6, '2019-04-29 23:51:41.929873', NULL, true, 0.00, 'EXPENSE', 'Faturas', 1);


--
-- TOC entry 3265 (class 0 OID 313047)
-- Dependencies: 240
-- Data for Name: telephones; Type: TABLE DATA; Schema: registration; Owner: -
--



--
-- TOC entry 3266 (class 0 OID 313052)
-- Dependencies: 241
-- Data for Name: vehicles; Type: TABLE DATA; Schema: registration; Owner: -
--



--
-- TOC entry 3267 (class 0 OID 313057)
-- Dependencies: 242
-- Data for Name: wallets; Type: TABLE DATA; Schema: registration; Owner: -
--



--
-- TOC entry 3268 (class 0 OID 313064)
-- Dependencies: 243
-- Data for Name: cards; Type: TABLE DATA; Schema: registration_audit; Owner: -
--



--
-- TOC entry 3269 (class 0 OID 313069)
-- Dependencies: 244
-- Data for Name: contacts; Type: TABLE DATA; Schema: registration_audit; Owner: -
--



--
-- TOC entry 3270 (class 0 OID 313077)
-- Dependencies: 245
-- Data for Name: cost_centers; Type: TABLE DATA; Schema: registration_audit; Owner: -
--

INSERT INTO registration_audit.cost_centers VALUES (1, 1, 0, true, 'rgba(222,88,188,1.0)', NULL, 0.00, 'Cartão de Crédito', 0.00, NULL);
INSERT INTO registration_audit.cost_centers VALUES (1, 2, 1, true, 'rgba(222,88,188,1.0)', NULL, 0.00, 'Cartão de Crédito', 0.00, NULL);
INSERT INTO registration_audit.cost_centers VALUES (1, 3, 1, true, 'rgba(222,88,188,1.0)', NULL, 0.00, 'Cartão de Crédito', 0.00, NULL);
INSERT INTO registration_audit.cost_centers VALUES (1, 4, 1, true, 'rgba(222,88,188,1.0)', NULL, 0.00, 'Cartão de Crédito', 0.00, NULL);


--
-- TOC entry 3271 (class 0 OID 313085)
-- Dependencies: 246
-- Data for Name: financial_periods; Type: TABLE DATA; Schema: registration_audit; Owner: -
--



--
-- TOC entry 3272 (class 0 OID 313090)
-- Dependencies: 247
-- Data for Name: movement_classes; Type: TABLE DATA; Schema: registration_audit; Owner: -
--

INSERT INTO registration_audit.movement_classes VALUES (6, 1, 0, true, 0.00, 'EXPENSE', 'Faturas', 1);


--
-- TOC entry 3273 (class 0 OID 313095)
-- Dependencies: 248
-- Data for Name: telephones; Type: TABLE DATA; Schema: registration_audit; Owner: -
--



--
-- TOC entry 3274 (class 0 OID 313100)
-- Dependencies: 249
-- Data for Name: vehicles; Type: TABLE DATA; Schema: registration_audit; Owner: -
--



--
-- TOC entry 3275 (class 0 OID 313105)
-- Dependencies: 250
-- Data for Name: wallets; Type: TABLE DATA; Schema: registration_audit; Owner: -
--



--
-- TOC entry 3286 (class 0 OID 0)
-- Dependencies: 253
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.hibernate_sequence', 1, false);


--
-- TOC entry 3287 (class 0 OID 0)
-- Dependencies: 254
-- Name: pooled_sequence_generator; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.pooled_sequence_generator', 176, true);


--
-- TOC entry 3288 (class 0 OID 0)
-- Dependencies: 251
-- Name: revisions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.revisions_id_seq', 4, true);


--
-- TOC entry 2939 (class 2606 OID 312756)
-- Name: authorizations authorizations_pkey; Type: CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.authorizations
    ADD CONSTRAINT authorizations_pkey PRIMARY KEY (id);


--
-- TOC entry 2941 (class 2606 OID 312761)
-- Name: configurations configurations_pkey; Type: CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.configurations
    ADD CONSTRAINT configurations_pkey PRIMARY KEY (id);


--
-- TOC entry 2943 (class 2606 OID 312766)
-- Name: grants grants_pkey; Type: CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.grants
    ADD CONSTRAINT grants_pkey PRIMARY KEY (id);


--
-- TOC entry 2945 (class 2606 OID 312771)
-- Name: groups groups_pkey; Type: CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id);


--
-- TOC entry 2947 (class 2606 OID 312776)
-- Name: profiles profiles_pkey; Type: CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.profiles
    ADD CONSTRAINT profiles_pkey PRIMARY KEY (id);


--
-- TOC entry 2949 (class 2606 OID 312786)
-- Name: users uk_rcle35tk5t6py9hf7uow9qkcw; Type: CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.users
    ADD CONSTRAINT uk_rcle35tk5t6py9hf7uow9qkcw UNIQUE (id_profile);


--
-- TOC entry 2951 (class 2606 OID 312784)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 2953 (class 2606 OID 312791)
-- Name: authorizations authorizations_pkey; Type: CONSTRAINT; Schema: configuration_audit; Owner: -
--

ALTER TABLE ONLY configuration_audit.authorizations
    ADD CONSTRAINT authorizations_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 2955 (class 2606 OID 312796)
-- Name: grants grants_pkey; Type: CONSTRAINT; Schema: configuration_audit; Owner: -
--

ALTER TABLE ONLY configuration_audit.grants
    ADD CONSTRAINT grants_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 2957 (class 2606 OID 312801)
-- Name: groups groups_pkey; Type: CONSTRAINT; Schema: configuration_audit; Owner: -
--

ALTER TABLE ONLY configuration_audit.groups
    ADD CONSTRAINT groups_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 2959 (class 2606 OID 312806)
-- Name: profiles profiles_pkey; Type: CONSTRAINT; Schema: configuration_audit; Owner: -
--

ALTER TABLE ONLY configuration_audit.profiles
    ADD CONSTRAINT profiles_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 2961 (class 2606 OID 312814)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: configuration_audit; Owner: -
--

ALTER TABLE ONLY configuration_audit.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 2963 (class 2606 OID 312819)
-- Name: apportionments apportionments_pkey; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.apportionments
    ADD CONSTRAINT apportionments_pkey PRIMARY KEY (id);


--
-- TOC entry 2967 (class 2606 OID 312824)
-- Name: closings closings_pkey; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.closings
    ADD CONSTRAINT closings_pkey PRIMARY KEY (id);


--
-- TOC entry 2969 (class 2606 OID 312829)
-- Name: credit_card_invoices credit_card_invoices_pkey; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.credit_card_invoices
    ADD CONSTRAINT credit_card_invoices_pkey PRIMARY KEY (id);


--
-- TOC entry 2973 (class 2606 OID 312834)
-- Name: launches launches_pkey; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.launches
    ADD CONSTRAINT launches_pkey PRIMARY KEY (id);


--
-- TOC entry 2979 (class 2606 OID 312842)
-- Name: movements movements_pkey; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.movements
    ADD CONSTRAINT movements_pkey PRIMARY KEY (id);


--
-- TOC entry 2983 (class 2606 OID 312847)
-- Name: payments payments_pkey; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id);


--
-- TOC entry 2985 (class 2606 OID 312855)
-- Name: transfers transfers_pkey; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.transfers
    ADD CONSTRAINT transfers_pkey PRIMARY KEY (id);


--
-- TOC entry 2975 (class 2606 OID 312940)
-- Name: launches uk_2evk157jbp5y9uie895ksm4vu; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.launches
    ADD CONSTRAINT uk_2evk157jbp5y9uie895ksm4vu UNIQUE (id_period_movement);


--
-- TOC entry 2971 (class 2606 OID 312938)
-- Name: credit_card_invoices uk_9pwfslfqrmaj7tmqf68xalgj6; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.credit_card_invoices
    ADD CONSTRAINT uk_9pwfslfqrmaj7tmqf68xalgj6 UNIQUE (identification);


--
-- TOC entry 2965 (class 2606 OID 312936)
-- Name: apportionments uk_i4o0xvmsa7c03dsnrhxgfa6k4; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.apportionments
    ADD CONSTRAINT uk_i4o0xvmsa7c03dsnrhxgfa6k4 UNIQUE (code);


--
-- TOC entry 2977 (class 2606 OID 312942)
-- Name: launches uk_jyrp61jkfjur3so4pcb65ddab; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.launches
    ADD CONSTRAINT uk_jyrp61jkfjur3so4pcb65ddab UNIQUE (code);


--
-- TOC entry 2981 (class 2606 OID 312944)
-- Name: movements uk_okyno9p8aeg5w3vs09qt6u41s; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.movements
    ADD CONSTRAINT uk_okyno9p8aeg5w3vs09qt6u41s UNIQUE (code);


--
-- TOC entry 2987 (class 2606 OID 312863)
-- Name: wallet_balances wallet_balances_pkey; Type: CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.wallet_balances
    ADD CONSTRAINT wallet_balances_pkey PRIMARY KEY (id);


--
-- TOC entry 2989 (class 2606 OID 312949)
-- Name: apportionments apportionments_pkey; Type: CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.apportionments
    ADD CONSTRAINT apportionments_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 2991 (class 2606 OID 312954)
-- Name: closings closings_pkey; Type: CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.closings
    ADD CONSTRAINT closings_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 2993 (class 2606 OID 312959)
-- Name: credit_card_invoices credit_card_invoices_pkey; Type: CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.credit_card_invoices
    ADD CONSTRAINT credit_card_invoices_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 2995 (class 2606 OID 312964)
-- Name: launches launches_pkey; Type: CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.launches
    ADD CONSTRAINT launches_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 2997 (class 2606 OID 312972)
-- Name: movements movements_pkey; Type: CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.movements
    ADD CONSTRAINT movements_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 2999 (class 2606 OID 312977)
-- Name: payments payments_pkey; Type: CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.payments
    ADD CONSTRAINT payments_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3001 (class 2606 OID 312985)
-- Name: transfers transfers_pkey; Type: CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.transfers
    ADD CONSTRAINT transfers_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3003 (class 2606 OID 312993)
-- Name: wallet_balances wallet_balances_pkey; Type: CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.wallet_balances
    ADD CONSTRAINT wallet_balances_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3005 (class 2606 OID 312998)
-- Name: fuels fuels_pkey; Type: CONSTRAINT; Schema: journal; Owner: -
--

ALTER TABLE ONLY journal.fuels
    ADD CONSTRAINT fuels_pkey PRIMARY KEY (id);


--
-- TOC entry 3007 (class 2606 OID 313003)
-- Name: refuelings refuelings_pkey; Type: CONSTRAINT; Schema: journal; Owner: -
--

ALTER TABLE ONLY journal.refuelings
    ADD CONSTRAINT refuelings_pkey PRIMARY KEY (id);


--
-- TOC entry 3009 (class 2606 OID 313005)
-- Name: refuelings uk_ar1odo65tv6vkncc7hcg6kq51; Type: CONSTRAINT; Schema: journal; Owner: -
--

ALTER TABLE ONLY journal.refuelings
    ADD CONSTRAINT uk_ar1odo65tv6vkncc7hcg6kq51 UNIQUE (code);


--
-- TOC entry 3011 (class 2606 OID 313010)
-- Name: fuels fuels_pkey; Type: CONSTRAINT; Schema: journal_audit; Owner: -
--

ALTER TABLE ONLY journal_audit.fuels
    ADD CONSTRAINT fuels_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3013 (class 2606 OID 313015)
-- Name: refuelings refuelings_pkey; Type: CONSTRAINT; Schema: journal_audit; Owner: -
--

ALTER TABLE ONLY journal_audit.refuelings
    ADD CONSTRAINT refuelings_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3049 (class 2606 OID 313117)
-- Name: revisions revisions_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.revisions
    ADD CONSTRAINT revisions_pkey PRIMARY KEY (id);


--
-- TOC entry 3015 (class 2606 OID 313020)
-- Name: cards cards_pkey; Type: CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.cards
    ADD CONSTRAINT cards_pkey PRIMARY KEY (id);


--
-- TOC entry 3017 (class 2606 OID 313028)
-- Name: contacts contacts_pkey; Type: CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.contacts
    ADD CONSTRAINT contacts_pkey PRIMARY KEY (id);


--
-- TOC entry 3021 (class 2606 OID 313036)
-- Name: cost_centers cost_centers_pkey; Type: CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.cost_centers
    ADD CONSTRAINT cost_centers_pkey PRIMARY KEY (id);


--
-- TOC entry 3023 (class 2606 OID 313041)
-- Name: financial_periods financial_periods_pkey; Type: CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.financial_periods
    ADD CONSTRAINT financial_periods_pkey PRIMARY KEY (id);


--
-- TOC entry 3025 (class 2606 OID 313046)
-- Name: movement_classes movement_classes_pkey; Type: CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.movement_classes
    ADD CONSTRAINT movement_classes_pkey PRIMARY KEY (id);


--
-- TOC entry 3027 (class 2606 OID 313051)
-- Name: telephones telephones_pkey; Type: CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.telephones
    ADD CONSTRAINT telephones_pkey PRIMARY KEY (id);


--
-- TOC entry 3019 (class 2606 OID 313063)
-- Name: contacts uk_b42wa7jxj2x5sb773fkrq7rsh; Type: CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.contacts
    ADD CONSTRAINT uk_b42wa7jxj2x5sb773fkrq7rsh UNIQUE (code);


--
-- TOC entry 3029 (class 2606 OID 313056)
-- Name: vehicles vehicles_pkey; Type: CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.vehicles
    ADD CONSTRAINT vehicles_pkey PRIMARY KEY (id);


--
-- TOC entry 3031 (class 2606 OID 313061)
-- Name: wallets wallets_pkey; Type: CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.wallets
    ADD CONSTRAINT wallets_pkey PRIMARY KEY (id);


--
-- TOC entry 3033 (class 2606 OID 313068)
-- Name: cards cards_pkey; Type: CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.cards
    ADD CONSTRAINT cards_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3035 (class 2606 OID 313076)
-- Name: contacts contacts_pkey; Type: CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.contacts
    ADD CONSTRAINT contacts_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3037 (class 2606 OID 313084)
-- Name: cost_centers cost_centers_pkey; Type: CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.cost_centers
    ADD CONSTRAINT cost_centers_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3039 (class 2606 OID 313089)
-- Name: financial_periods financial_periods_pkey; Type: CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.financial_periods
    ADD CONSTRAINT financial_periods_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3041 (class 2606 OID 313094)
-- Name: movement_classes movement_classes_pkey; Type: CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.movement_classes
    ADD CONSTRAINT movement_classes_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3043 (class 2606 OID 313099)
-- Name: telephones telephones_pkey; Type: CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.telephones
    ADD CONSTRAINT telephones_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3045 (class 2606 OID 313104)
-- Name: vehicles vehicles_pkey; Type: CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.vehicles
    ADD CONSTRAINT vehicles_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3047 (class 2606 OID 313109)
-- Name: wallets wallets_pkey; Type: CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.wallets
    ADD CONSTRAINT wallets_pkey PRIMARY KEY (id, revision);


--
-- TOC entry 3054 (class 2606 OID 313142)
-- Name: users fk6ygxt7686dodh2vla65342ct4; Type: FK CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.users
    ADD CONSTRAINT fk6ygxt7686dodh2vla65342ct4 FOREIGN KEY (id_group) REFERENCES configuration.groups(id);


--
-- TOC entry 3055 (class 2606 OID 313147)
-- Name: users fk9wbwuppwb63kwaj2s6aaqi3th; Type: FK CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.users
    ADD CONSTRAINT fk9wbwuppwb63kwaj2s6aaqi3th FOREIGN KEY (id_profile) REFERENCES configuration.profiles(id);


--
-- TOC entry 3052 (class 2606 OID 313132)
-- Name: grants fkchpf69027hrvhv43xo5ijglg9; Type: FK CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.grants
    ADD CONSTRAINT fkchpf69027hrvhv43xo5ijglg9 FOREIGN KEY (id_group) REFERENCES configuration.groups(id);


--
-- TOC entry 3053 (class 2606 OID 313137)
-- Name: groups fkjofv20ia9udcgcbtkows5p05; Type: FK CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.groups
    ADD CONSTRAINT fkjofv20ia9udcgcbtkows5p05 FOREIGN KEY (id_parent) REFERENCES configuration.groups(id);


--
-- TOC entry 3050 (class 2606 OID 313122)
-- Name: configurations fkntxsje2agjhwaoy9bv0uj8sda; Type: FK CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.configurations
    ADD CONSTRAINT fkntxsje2agjhwaoy9bv0uj8sda FOREIGN KEY (id_credit_card_class) REFERENCES registration.movement_classes(id);


--
-- TOC entry 3051 (class 2606 OID 313127)
-- Name: grants fkojxnxlqxe5rkbrn1ebp14hkhf; Type: FK CONSTRAINT; Schema: configuration; Owner: -
--

ALTER TABLE ONLY configuration.grants
    ADD CONSTRAINT fkojxnxlqxe5rkbrn1ebp14hkhf FOREIGN KEY (id_authorization) REFERENCES configuration.authorizations(id);


--
-- TOC entry 3056 (class 2606 OID 313152)
-- Name: authorizations fk22ub5uj9vtbapmrymbj42nl5y; Type: FK CONSTRAINT; Schema: configuration_audit; Owner: -
--

ALTER TABLE ONLY configuration_audit.authorizations
    ADD CONSTRAINT fk22ub5uj9vtbapmrymbj42nl5y FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3060 (class 2606 OID 313172)
-- Name: users fka52fjge29y0d6mdxu0m11jgu7; Type: FK CONSTRAINT; Schema: configuration_audit; Owner: -
--

ALTER TABLE ONLY configuration_audit.users
    ADD CONSTRAINT fka52fjge29y0d6mdxu0m11jgu7 FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3058 (class 2606 OID 313162)
-- Name: groups fkaxo8cbaxjeknqqnqn9styfwhb; Type: FK CONSTRAINT; Schema: configuration_audit; Owner: -
--

ALTER TABLE ONLY configuration_audit.groups
    ADD CONSTRAINT fkaxo8cbaxjeknqqnqn9styfwhb FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3057 (class 2606 OID 313157)
-- Name: grants fkb4b62ghhbe0pff4fvfqkta69w; Type: FK CONSTRAINT; Schema: configuration_audit; Owner: -
--

ALTER TABLE ONLY configuration_audit.grants
    ADD CONSTRAINT fkb4b62ghhbe0pff4fvfqkta69w FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3059 (class 2606 OID 313167)
-- Name: profiles fkfj2mv9xe8nspucmwvk5njo50t; Type: FK CONSTRAINT; Schema: configuration_audit; Owner: -
--

ALTER TABLE ONLY configuration_audit.profiles
    ADD CONSTRAINT fkfj2mv9xe8nspucmwvk5njo50t FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3063 (class 2606 OID 313187)
-- Name: apportionments fk1k3on47q3sa5djlaxr3hmiaxf; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.apportionments
    ADD CONSTRAINT fk1k3on47q3sa5djlaxr3hmiaxf FOREIGN KEY (id_movement_class) REFERENCES registration.movement_classes(id);


--
-- TOC entry 3067 (class 2606 OID 313207)
-- Name: credit_card_invoices fk2g4g52jpylegb52vetxw0ybbo; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.credit_card_invoices
    ADD CONSTRAINT fk2g4g52jpylegb52vetxw0ybbo FOREIGN KEY (id_period_movement) REFERENCES financial.movements(id);


--
-- TOC entry 3064 (class 2606 OID 313192)
-- Name: closings fk5nrul75jot8m7akihstd5hvox; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.closings
    ADD CONSTRAINT fk5nrul75jot8m7akihstd5hvox FOREIGN KEY (id_financial_period) REFERENCES registration.financial_periods(id);


--
-- TOC entry 3069 (class 2606 OID 313217)
-- Name: launches fk5u1vu0ggfp660eal77ni26i1p; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.launches
    ADD CONSTRAINT fk5u1vu0ggfp660eal77ni26i1p FOREIGN KEY (id_fixed_movement) REFERENCES financial.movements(id);


--
-- TOC entry 3076 (class 2606 OID 313252)
-- Name: payments fk6f4mm78pt9hht622ano289ii5; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.payments
    ADD CONSTRAINT fk6f4mm78pt9hht622ano289ii5 FOREIGN KEY (id_wallet) REFERENCES registration.wallets(id);


--
-- TOC entry 3065 (class 2606 OID 313197)
-- Name: credit_card_invoices fk6py572qmmr98v9y8pjfkk71oe; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.credit_card_invoices
    ADD CONSTRAINT fk6py572qmmr98v9y8pjfkk71oe FOREIGN KEY (id_card) REFERENCES registration.cards(id);


--
-- TOC entry 3075 (class 2606 OID 313247)
-- Name: payments fk85sn64uu1y99kgdcgh4amdhe1; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.payments
    ADD CONSTRAINT fk85sn64uu1y99kgdcgh4amdhe1 FOREIGN KEY (id_card) REFERENCES registration.cards(id);


--
-- TOC entry 3066 (class 2606 OID 313202)
-- Name: credit_card_invoices fkcacod5eefyjx6qgtf99an3x5; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.credit_card_invoices
    ADD CONSTRAINT fkcacod5eefyjx6qgtf99an3x5 FOREIGN KEY (id_financial_period) REFERENCES registration.financial_periods(id);


--
-- TOC entry 3068 (class 2606 OID 313212)
-- Name: launches fkee46ii38ostt7s88vtgruwa7l; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.launches
    ADD CONSTRAINT fkee46ii38ostt7s88vtgruwa7l FOREIGN KEY (id_financial_period) REFERENCES registration.financial_periods(id);


--
-- TOC entry 3072 (class 2606 OID 313232)
-- Name: movements fkejqtkep4l3lnygxqajeuip0ol; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.movements
    ADD CONSTRAINT fkejqtkep4l3lnygxqajeuip0ol FOREIGN KEY (id_credit_card_invoice) REFERENCES financial.credit_card_invoices(id);


--
-- TOC entry 3073 (class 2606 OID 313237)
-- Name: movements fki4f2axo5kds2hrueh5t0paiun; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.movements
    ADD CONSTRAINT fki4f2axo5kds2hrueh5t0paiun FOREIGN KEY (id_financial_period) REFERENCES registration.financial_periods(id);


--
-- TOC entry 3077 (class 2606 OID 313257)
-- Name: transfers fkmm6x1wiu7nmihj9wh2q30q79y; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.transfers
    ADD CONSTRAINT fkmm6x1wiu7nmihj9wh2q30q79y FOREIGN KEY (id_destination) REFERENCES registration.wallets(id);


--
-- TOC entry 3079 (class 2606 OID 313267)
-- Name: wallet_balances fkmxbbwuerajw3d6bk0htwp78ao; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.wallet_balances
    ADD CONSTRAINT fkmxbbwuerajw3d6bk0htwp78ao FOREIGN KEY (id_wallet) REFERENCES registration.wallets(id);


--
-- TOC entry 3071 (class 2606 OID 313227)
-- Name: movements fkoa0ex8gxp3sjaotd56caclj4a; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.movements
    ADD CONSTRAINT fkoa0ex8gxp3sjaotd56caclj4a FOREIGN KEY (id_contact) REFERENCES registration.contacts(id);


--
-- TOC entry 3078 (class 2606 OID 313262)
-- Name: transfers fkom7ernk11c1g9itpqxjna0aso; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.transfers
    ADD CONSTRAINT fkom7ernk11c1g9itpqxjna0aso FOREIGN KEY (id_origin) REFERENCES registration.wallets(id);


--
-- TOC entry 3074 (class 2606 OID 313242)
-- Name: movements fkp4kx80q6q7mpcmplvk5osgcbe; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.movements
    ADD CONSTRAINT fkp4kx80q6q7mpcmplvk5osgcbe FOREIGN KEY (id_payment) REFERENCES financial.payments(id);


--
-- TOC entry 3062 (class 2606 OID 313182)
-- Name: apportionments fkrfwii6f9ddnneyhc0hfgvxp55; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.apportionments
    ADD CONSTRAINT fkrfwii6f9ddnneyhc0hfgvxp55 FOREIGN KEY (id_movement) REFERENCES financial.movements(id);


--
-- TOC entry 3070 (class 2606 OID 313222)
-- Name: launches fks2pvhi0xyabdf6ciurcvoc7yr; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.launches
    ADD CONSTRAINT fks2pvhi0xyabdf6ciurcvoc7yr FOREIGN KEY (id_period_movement) REFERENCES financial.movements(id);


--
-- TOC entry 3061 (class 2606 OID 313177)
-- Name: apportionments fktko0m6al48sia8sq6ltgqufgg; Type: FK CONSTRAINT; Schema: financial; Owner: -
--

ALTER TABLE ONLY financial.apportionments
    ADD CONSTRAINT fktko0m6al48sia8sq6ltgqufgg FOREIGN KEY (id_cost_center) REFERENCES registration.cost_centers(id);


--
-- TOC entry 3082 (class 2606 OID 313282)
-- Name: credit_card_invoices fk234dcifml3fw8sfqaein4i6hg; Type: FK CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.credit_card_invoices
    ADD CONSTRAINT fk234dcifml3fw8sfqaein4i6hg FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3085 (class 2606 OID 313297)
-- Name: payments fkctocrvj15mbvdh2o41wrhsb9s; Type: FK CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.payments
    ADD CONSTRAINT fkctocrvj15mbvdh2o41wrhsb9s FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3083 (class 2606 OID 313287)
-- Name: launches fkf4vlc4dunc02ansofr2a879rn; Type: FK CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.launches
    ADD CONSTRAINT fkf4vlc4dunc02ansofr2a879rn FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3081 (class 2606 OID 313277)
-- Name: closings fkm99g1bfmtv5v12xgf9n9c5ajc; Type: FK CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.closings
    ADD CONSTRAINT fkm99g1bfmtv5v12xgf9n9c5ajc FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3087 (class 2606 OID 313307)
-- Name: wallet_balances fknwhxtwa9m98jvmgxjk0v1r6q6; Type: FK CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.wallet_balances
    ADD CONSTRAINT fknwhxtwa9m98jvmgxjk0v1r6q6 FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3086 (class 2606 OID 313302)
-- Name: transfers fkq5qjvkw8wcn1365yrxm4b7c24; Type: FK CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.transfers
    ADD CONSTRAINT fkq5qjvkw8wcn1365yrxm4b7c24 FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3080 (class 2606 OID 313272)
-- Name: apportionments fkt31ed1oymg5c7cq04h67wm282; Type: FK CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.apportionments
    ADD CONSTRAINT fkt31ed1oymg5c7cq04h67wm282 FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3084 (class 2606 OID 313292)
-- Name: movements fktqaualo4svern1b3en8eoyuov; Type: FK CONSTRAINT; Schema: financial_audit; Owner: -
--

ALTER TABLE ONLY financial_audit.movements
    ADD CONSTRAINT fktqaualo4svern1b3en8eoyuov FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3091 (class 2606 OID 313327)
-- Name: refuelings fk23lh4w400w3xusj629wuwuk2r; Type: FK CONSTRAINT; Schema: journal; Owner: -
--

ALTER TABLE ONLY journal.refuelings
    ADD CONSTRAINT fk23lh4w400w3xusj629wuwuk2r FOREIGN KEY (id_period_movement) REFERENCES financial.movements(id);


--
-- TOC entry 3092 (class 2606 OID 313332)
-- Name: refuelings fk2ku1ybi0l8l162hnn77cy18qr; Type: FK CONSTRAINT; Schema: journal; Owner: -
--

ALTER TABLE ONLY journal.refuelings
    ADD CONSTRAINT fk2ku1ybi0l8l162hnn77cy18qr FOREIGN KEY (id_vehicle) REFERENCES registration.vehicles(id);


--
-- TOC entry 3089 (class 2606 OID 313317)
-- Name: refuelings fk7gmos0wbwi6kxfhnbe3u860vp; Type: FK CONSTRAINT; Schema: journal; Owner: -
--

ALTER TABLE ONLY journal.refuelings
    ADD CONSTRAINT fk7gmos0wbwi6kxfhnbe3u860vp FOREIGN KEY (id_financial_period) REFERENCES registration.financial_periods(id);


--
-- TOC entry 3090 (class 2606 OID 313322)
-- Name: refuelings fkg9u670luia5ftqd8bkiw4ui2l; Type: FK CONSTRAINT; Schema: journal; Owner: -
--

ALTER TABLE ONLY journal.refuelings
    ADD CONSTRAINT fkg9u670luia5ftqd8bkiw4ui2l FOREIGN KEY (id_movement_class) REFERENCES registration.movement_classes(id);


--
-- TOC entry 3088 (class 2606 OID 313312)
-- Name: fuels fksr45pcrr9j0yewu3t6kg9y3l4; Type: FK CONSTRAINT; Schema: journal; Owner: -
--

ALTER TABLE ONLY journal.fuels
    ADD CONSTRAINT fksr45pcrr9j0yewu3t6kg9y3l4 FOREIGN KEY (id_refueling) REFERENCES journal.refuelings(id);


--
-- TOC entry 3093 (class 2606 OID 313337)
-- Name: fuels fkc7xugi6j865xeikkq5fwlfa4d; Type: FK CONSTRAINT; Schema: journal_audit; Owner: -
--

ALTER TABLE ONLY journal_audit.fuels
    ADD CONSTRAINT fkc7xugi6j865xeikkq5fwlfa4d FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3094 (class 2606 OID 313342)
-- Name: refuelings fkd1q1lybqpo9mkkclav9mtd4o8; Type: FK CONSTRAINT; Schema: journal_audit; Owner: -
--

ALTER TABLE ONLY journal_audit.refuelings
    ADD CONSTRAINT fkd1q1lybqpo9mkkclav9mtd4o8 FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3095 (class 2606 OID 313347)
-- Name: cards fk23q06abj2wxsha1s3k7a39q26; Type: FK CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.cards
    ADD CONSTRAINT fk23q06abj2wxsha1s3k7a39q26 FOREIGN KEY (id_wallet) REFERENCES registration.wallets(id);


--
-- TOC entry 3097 (class 2606 OID 313357)
-- Name: movement_classes fkcu5756n4mkvoo5co1o3opvmpm; Type: FK CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.movement_classes
    ADD CONSTRAINT fkcu5756n4mkvoo5co1o3opvmpm FOREIGN KEY (id_cost_center) REFERENCES registration.cost_centers(id);


--
-- TOC entry 3099 (class 2606 OID 313367)
-- Name: vehicles fkerjyr8yb86ao5r2kg1yd0ipuh; Type: FK CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.vehicles
    ADD CONSTRAINT fkerjyr8yb86ao5r2kg1yd0ipuh FOREIGN KEY (id_cost_center) REFERENCES registration.cost_centers(id);


--
-- TOC entry 3098 (class 2606 OID 313362)
-- Name: telephones fkk6dy0y4cym2scrgmocs1k4hwl; Type: FK CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.telephones
    ADD CONSTRAINT fkk6dy0y4cym2scrgmocs1k4hwl FOREIGN KEY (id_contact) REFERENCES registration.contacts(id);


--
-- TOC entry 3096 (class 2606 OID 313352)
-- Name: cost_centers fku7nth5qris3q90i0opy66966; Type: FK CONSTRAINT; Schema: registration; Owner: -
--

ALTER TABLE ONLY registration.cost_centers
    ADD CONSTRAINT fku7nth5qris3q90i0opy66966 FOREIGN KEY (id_parent) REFERENCES registration.cost_centers(id);


--
-- TOC entry 3101 (class 2606 OID 313377)
-- Name: contacts fkfvix3l00gnmp983gsf2xyvy9t; Type: FK CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.contacts
    ADD CONSTRAINT fkfvix3l00gnmp983gsf2xyvy9t FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3100 (class 2606 OID 313372)
-- Name: cards fkhlxinbg71nfyf3vg398ex89ru; Type: FK CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.cards
    ADD CONSTRAINT fkhlxinbg71nfyf3vg398ex89ru FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3103 (class 2606 OID 313387)
-- Name: financial_periods fkhp7unc529vwy657v2fhvou347; Type: FK CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.financial_periods
    ADD CONSTRAINT fkhp7unc529vwy657v2fhvou347 FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3102 (class 2606 OID 313382)
-- Name: cost_centers fki5rafg5vo2oao9d27deco8ops; Type: FK CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.cost_centers
    ADD CONSTRAINT fki5rafg5vo2oao9d27deco8ops FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3105 (class 2606 OID 313397)
-- Name: telephones fkjgebt7hqh0fjh0i1p3c2lbapn; Type: FK CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.telephones
    ADD CONSTRAINT fkjgebt7hqh0fjh0i1p3c2lbapn FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3104 (class 2606 OID 313392)
-- Name: movement_classes fkknk393hua2xicasxbaxoggo83; Type: FK CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.movement_classes
    ADD CONSTRAINT fkknk393hua2xicasxbaxoggo83 FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3106 (class 2606 OID 313402)
-- Name: vehicles fkqaich7ec9w4cftkboml7chsa5; Type: FK CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.vehicles
    ADD CONSTRAINT fkqaich7ec9w4cftkboml7chsa5 FOREIGN KEY (revision) REFERENCES public.revisions(id);


--
-- TOC entry 3107 (class 2606 OID 313407)
-- Name: wallets fksm0lyf7ty9p2rt248yk5yauna; Type: FK CONSTRAINT; Schema: registration_audit; Owner: -
--

ALTER TABLE ONLY registration_audit.wallets
    ADD CONSTRAINT fksm0lyf7ty9p2rt248yk5yauna FOREIGN KEY (revision) REFERENCES public.revisions(id);


-- Completed on 2019-04-30 02:55:09 UTC

--
-- PostgreSQL database dump complete
--