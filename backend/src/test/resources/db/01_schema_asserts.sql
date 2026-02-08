-- 01_schema_asserts.sql
DO $$
BEGIN
  -- tables
  IF to_regclass('public.customer') IS NULL THEN RAISE EXCEPTION 'missing table: customer'; END IF;
  IF to_regclass('public.orders')   IS NULL THEN RAISE EXCEPTION 'missing table: orders'; END IF;
  IF to_regclass('public.order_line') IS NULL THEN RAISE EXCEPTION 'missing table: order_line'; END IF;
  IF to_regclass('public.forfait')  IS NULL THEN RAISE EXCEPTION 'missing table: forfait'; END IF;

  -- FK (noms auto générés: on teste par “relation” plutôt que par nom)
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint c
    WHERE c.contype='f'
      AND c.conrelid='public.orders'::regclass
      AND c.confrelid='public.customer'::regclass
  ) THEN RAISE EXCEPTION 'missing FK: orders -> customer'; END IF;

  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint c
    WHERE c.contype='f'
      AND c.conrelid='public.order_line'::regclass
      AND c.confrelid='public.orders'::regclass
  ) THEN RAISE EXCEPTION 'missing FK: order_line -> orders'; END IF;

  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint c
    WHERE c.contype='f'
      AND c.conrelid='public.forfait'::regclass
      AND c.confrelid='public.customer'::regclass
  ) THEN RAISE EXCEPTION 'missing FK: forfait -> customer'; END IF;

  -- indexes “nécessaires” (à adapter à TES attentes réelles)
  IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE schemaname='public' AND tablename='customer' AND indexname='customer_pkey')
  THEN RAISE EXCEPTION 'missing index: customer_pkey'; END IF;
END $$;
