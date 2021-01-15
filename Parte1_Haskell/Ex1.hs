module Ex1 ( --creation of the module Ex1
  ListBag(..),
  wf,
  empty,
  singleton,
  fromList,
  isEmpty,
  mul,
  toList,
  sumBag,
  ) where

data ListBag a = LB [(a, Int)] deriving (Show, Eq)


-- wf :: Eq a => ListBag a -> Bool
wf (LB []) = True --case of empty list
wf (LB (x:xs)) = --case of not empty list
    let l = length([ y | y <- xs, (fst y)==(fst x)]) in --n° of elements with same key of x
        if l == 0 then wf (LB xs) --there are no element with same key
                else False --there are element with the same key


--CONSTRUCTORS

--empty :: ListBag a
empty = LB [] 

--singleton :: a -> ListBag a
singleton v = LB [(v,1)] 

--fromList :: Eq a => [a] -> ListBag a
fromList lst = 
    let createlist l = case l of --function to construct element of ListBag
            [] -> [] --case of empty list (base case)
            (x:xs) -> (x,length(filter (==x) xs)+1) : createlist (filter (/=x) xs) --element with key of x and recursion on the other keys
    in
        LB (createlist lst) --creation of the ListBag




--OPERATIONS

--isEmpty :: ListBag a -> Bool
isEmpty bag=
    case bag of
        LB [] -> True       --the list is empty
        LB (x:xs) -> False  --the list is not empty


-- mul :: Eq a => a -> ListBag a -> Int
mul v bag = 
    case (v, bag) of 
        (_, (LB [])) -> 0       --the list is empty 
        (v, (LB (x:xs))) ->     --the list is not empty
            if v == fst x then snd x    --if v is found return multeplicity
            else mul v (LB xs)          --if v is not found recurring on other elements


--toList :: ListBag a -> [a]
toList bag = 
    case bag of
        LB [] -> []     --the list is empty
        LB ((v,m):xs) -> (take m (repeat v)) ++ (toList (LB xs)) --take the first m element of a repetition 
            --of v and concatenate to the recursion on the rest of the elements


--sumBag :: Eq a => ListBag a -> ListBag a -> ListBag a
sumBag bag bag' = fromList((toList (bag) ++ toList(bag')))
--create a list with all elements of bag and bag' and from it create ListBag