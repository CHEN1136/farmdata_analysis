package cn.com.chinahitech.hotback.market.entity;

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
public class Market implements Serializable {

    private static final long serialVersionUID = 1L;

    private String marketName;

    private String marketRegion;


}
