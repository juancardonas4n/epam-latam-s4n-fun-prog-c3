def funcion(u:(Int,Int),v:(Int,Int)) = if (u._2 == v._1) 
                                          if (u._2 == 0) u._1 + v._2 
                                          else if (u._2 == 1) u._1 - v._2
                                               else u._1 * v._2
                                       else 0
