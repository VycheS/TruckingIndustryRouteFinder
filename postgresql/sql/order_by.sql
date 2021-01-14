-- это для примера. Не имеет смысла для проекта в последующем будет удалено
SELECT c.name, coalesce(sum(p.price), 0) as orders_sum
FROM customer c
LEFT OUTER JOIN cart
ON c.id = cart.customer_id
LEFT OUTER JOIN cart_product cp
ON cp.cart_id = cart.id
LEFT OUTER JOIN product p
ON p.id = cp.product_id
GROUP BY c.name
ORDER BY orders_sum DESC;