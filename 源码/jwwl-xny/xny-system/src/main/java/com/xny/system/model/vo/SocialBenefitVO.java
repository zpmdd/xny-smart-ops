package com.xny.system.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SocialBenefitVO {

    public SocialBenefitVO(){

    }

    /**
     * @param pg 发电量
     */
    public SocialBenefitVO(Double pg){
        if(null == pg){
            pg = 0d;
        }
        this.carbonDioxide = pg * 1;
        this.coalConserve  = pg * 2;
        this.dustReduction = pg * 3;
        this.forestRebuild = pg * 4;
        this.newTreesAdded = pg * 5;
    }

    @ApiModelProperty(value = "二氧化碳减排")
    private Double carbonDioxide;

    @ApiModelProperty(value = "标煤节约")
    private Double coalConserve;

    @ApiModelProperty(value = "粉尘减排")
    private Double dustReduction;

    @ApiModelProperty(value = "森林再造")
    private Double forestRebuild;

    @ApiModelProperty(value = "树木新增")
    private Double newTreesAdded;

}
