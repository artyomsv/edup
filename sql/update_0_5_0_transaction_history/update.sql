ALTER TABLE public.transaction_type ADD glyph_icon VARCHAR(128) NULL;

UPDATE public.transaction_type SET  glyph_icon = 'glyphicon glyphicon-euro' WHERE transaction_type_id = 1;
UPDATE public.transaction_type SET  glyph_icon = 'glyphicon glyphicon-education' WHERE transaction_type_id = 3;
UPDATE public.transaction_type SET  glyph_icon = 'glyphicon glyphicon-credit-card' WHERE transaction_type_id = 2;