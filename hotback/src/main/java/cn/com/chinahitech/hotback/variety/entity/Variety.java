package cn.com.chinahitech.hotback.variety.entity;

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
public class Variety implements Serializable {

    private static final long serialVersionUID = 1L;

    private String varietyName;

    private String categoryName;

    private String anotherName;


}
