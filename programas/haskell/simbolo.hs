-- Un tipo de dato algebraico

-- Unión (Suma):

data Símbolo = SmC Char
             | SmI Int
             deriving Show

SmI :: Int -> Símbolo

