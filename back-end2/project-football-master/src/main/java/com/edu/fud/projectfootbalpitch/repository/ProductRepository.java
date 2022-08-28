package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.ProductsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<ProductsEntity,Long> {

    @Query(value = "select count(id) from products",nativeQuery = true)
    int TotalProduct();
    @Query(value = "select sum(o.quantity) as tongSoLuongBan from products p\n" +
            " inner join order_detail as o\n" +
            " on o.product_id = p.id where p.id=:productId group by p.id",nativeQuery = true)
    int sumQuantityProductSellByProductId(long productId);
    @Query(value = "select p.*,sum(o.quantity) as tongSoLuongBan from products p\n" +
            " inner join order_detail as o\n" +
            " on o.product_id = p.id group by p.id order by tongSoLuongBan desc limit 5",nativeQuery = true)
    List<ProductsEntity> findAllTop5ProductSell();
    @Query(value = "select sum(p.price*o.quantity) as tongSoLuongBan from products p\n" +
            " inner join order_detail as o\n" +
            " on o.product_id = p.id where p.id=:productId group by p.id",nativeQuery = true)
    double sumTotalPriceProductSellByProductId(long productId);

    @Query(value = "select * from products where quantity>0 and id=:id",nativeQuery = true)
    ProductsEntity checkQuantity(Long id);

//    @Query(value = "select price from  where ",nativeQuery = true)
//    double getPriceProduct(Long id);
    //duc
    @Query(value = "SELECT * FROM products where (status = 1 and quantity > 0) order by modifieddate DESC limit 8",nativeQuery = true)
    List<ProductsEntity> findLimitByDate();

    @Query(value = "SELECT * FROM products where (status = 1 and quantity > 0 and  name LIKE %:keyword% ) order by modifieddate DESC ",nativeQuery = true)
    List<ProductsEntity> findAllPagined(String keyword);

    @Query(value = "SELECT * FROM products WHERE id = :proId",nativeQuery = true)
    List<ProductsEntity> findOneByID(long proId);

    @Query(value = "SELECT * FROM products where (status = 1 and quantity > 0 and category_product_id = :cateId ) order by modifieddate DESC ",nativeQuery = true)
    List<ProductsEntity> findAllByCate(long cateId);

    @Query(value = "SELECT * FROM products WHERE id = :proId",nativeQuery = true)
    ProductsEntity findAllByID(long proId);

    @Modifying
    @Query(value = "update products set quantity = (quantity-:quantity) where id = :id", nativeQuery = true)
    void updateQuantity(int quantity, long id);

    //chatbot
    @Query(value = "select p.* from products p \n" +
            "inner join category_products cp on cp.id = p.category_product_id\n" +
            "where p.name like %:name% or cp.name like %:name% \n" +
            "and (p.status = 1 and p.quantity > 0);",nativeQuery = true)
    List<ProductsEntity> findAllProductByNameOrCate(String name);

    @Query(value = "SELECT * FROM products \n" +
            "where (status = 1 and quantity > 0) order by createddate DESC  limit 5;",nativeQuery = true)
    List<ProductsEntity> findFiveProductByLastCreateDate();
}
