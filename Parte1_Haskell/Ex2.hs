module Ex2 ( --creation of module Ex2
  mapLB,
  ) where

import Ex1


instance Foldable ListBag where
    --definition of foldr on ListBag
    foldr f z (LB []) = z
    foldr f z (LB ((v,m):xs)) = f v (foldr f z (LB xs))

    --definition of foldl on ListBag
    foldl f z (LB []) = z
    foldl f z (LB ((v,m):xs)) = foldl f (f z v) (LB xs)


--mapLB :: (t -> a) -> ListBag t -> ListBag a
mapLB f (LB x) = 
    let mapFirst f l = case l of  --function that apply f to keys of elements of the list
            [] -> []              --case of empty list
            ((v,m):xs) -> ((f v),m):(mapFirst f xs)   --case of not empty list
    in
        LB (mapFirst f x)



--instance Functor ListBag where
--    fmap = mapLB

--A type f is a Functor if it provides a function fmap which, 
--given any types a and b lets you apply any function from (a -> b) 
--to turn an f a into an f b, preserving the structure of f
--Furthermore f needs to adhere to the following:
--1. Identity
--2. Composition

--we can notice that the structure of f can be broken using fmap = mapLB
--because ListBag are well-formed (look also at the previous exercise)
--but applying fmap it may no longer be true, for example in the case of use of a 
--function that return always the same value (a costant).
--In this case, we will have a ListBag with a key that appears in more than one
--pair, but in a multiset respresented with pairs (key, multiplicity) we have 
--only one pair for every key that appears in it.