-- Los tipos se pueden clasificar

class Eq a where
  (==) :: a -> a -> Bool
  (/=) :: a -> a -> Bool


instance Eq Int where
  x == y = ...
  x /= y = not (x == y)


instance Eq Double where
  x == y = ...
  x /= y = not (x == y)

