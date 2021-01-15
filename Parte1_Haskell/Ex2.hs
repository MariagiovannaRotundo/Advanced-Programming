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

--A constrain that we have on ListBag is the Eq on the elements 
--using fmap = mapLB we can possibly violate this constrain onto the 
--ListBag because we can obtain (thanks to the function we apply) 
--a type b (in ListBag b) that does not support Eq.