-- Un tipo de dato algebraico

-- Producto

data Complejo = Cplj Double Double
              deriving Show

-- Cplj :: Double -> Double -> Complejo

-- Producto (Registro)

data Complejo = Cplj { real :: Double,
                       img  :: Double
                     } deriving Show

-- Cplj :: Double -> Double -> Complejo
-- real :: Complejo -> Double

