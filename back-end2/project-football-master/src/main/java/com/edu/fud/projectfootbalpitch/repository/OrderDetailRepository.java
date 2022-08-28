package com.edu.fud.projectfootbalpitch.repository;

import com.edu.fud.projectfootbalpitch.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity,Long> {
    @Query(value = "select d.* from orders o inner join order_detail d on o.id=d.order_id where o.id=:orderId",
            nativeQuery = true)
    List<OrderDetailEntity> findAllByOrderId(long orderId);
    @Query(value = "select month(o.createddate) month  from products p\n" +
            " inner join order_detail o on o.product_id=p.id\n" +
            "group by month(o.createddate) order by  month(o.createddate) asc",nativeQuery = true)
    int[] findAllMonthOfProducts();
    @Query(value = "select sum(p.price*o.quantity) as soTienThuDuocTrongThang from products p\n" +
            " inner join order_detail o on o.product_id=p.id where month(o.createddate)=:month",nativeQuery = true)
    double findAllTotalPriceOfMonthProducts(int month);
    @Query(value = "select sum(p.price*o.quantity) as soTienThuDuocTrongThang from products p\n" +
            " inner join order_detail o on o.product_id=p.id where month(o.createddate)=:month",nativeQuery = true)
    Double findAllTotalPriceOfMonthProductsCheck(int month);
    //huy
    @Query(value ="select od.*,p.*,u.id from user u inner join orders o inner join order_detail od inner join products p\n" +
            " on o.id=od.order_id where o.id= :orderID and u.id=:userId group by od.id;\n",nativeQuery = true)
    List<OrderDetailEntity> findAllByOrderID(long orderID,long userId);
}
