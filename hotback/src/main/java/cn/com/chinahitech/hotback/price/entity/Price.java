package cn.com.chinahitech.hotback.price.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhuminghang
 * @since 2020-07-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Price implements Serializable {

    private static final long serialVersionUID = 1L;

    private String region;

    private String marketName;

    private String varietyName;

    private Double highestPrice;

    private Double lowestPrice;

    private Double avgPrice;

    private String date;


}
