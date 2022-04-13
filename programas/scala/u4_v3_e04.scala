
def doblar[M[_]:Monad](m:M[Int]):M[Int] = for {
  v <- m
  nv = v * 2
} yield nv
