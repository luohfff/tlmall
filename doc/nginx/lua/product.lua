-- 导入lua-resty-template函数库
local template = require('resty.template')
local flashPromotionId = ngx.req.arg_flashPromotionId
ngx.log(ngx.ERR, "秒杀活动ID: ", flashPromotionId)
local promotionProductId = ngx.req.arg_promotionProductId
ngx.log(ngx.ERR, "秒杀产品ID: ", promotionProductId)
local templateName = "seckill_"..flashPromotionId.."_"..promotionProductId..".html"
ngx.log(ngx.ERR, "秒杀产品模板文件: ", templateName)
local memberId = ngx.req.get_headers()["memberId"]
ngx.log(ngx.ERR, "渲染页面输出，获得当前用户ID: ", memberId)
template.render(templateName, memberId)