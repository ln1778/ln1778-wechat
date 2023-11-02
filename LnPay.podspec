
require "json"
version = JSON.parse(File.read("package.json"))["version"]


Pod::Spec.new do |s|
  s.name         = "LnPay"
  s.version      = version
  s.summary      = "Alipay,Wechat SDK for React Native. "
  s.description  = "Wechat,Alipay SDK for React Native. "
  s.author       = { "ln1778" => "liunan1776@gmail.com" }
  s.homepage     = "https://github.com/ln1778/ln1778-wechat"
  s.license      = "MIT"
  s.platform     = :ios, "12.4"
  s.source       = { :git => "https://github.com/ln1778/ln1778-wechat.git",tag: "v" + s.version.to_s }
  s.source_files  = "ios/*.{h,m}"
  s.resource = 'AlipaySDK.bundle'
  s.dependency "React"
  s.vendored_libraries = 'ios/libWeChatSDK.a'
  s.requires_arc = true
  s.frameworks = 'SystemConfiguration','CoreTelephony', 'QuartzCore', 'CoreText', "CoreGraphics", "UIKit", "Foundation", "CFNetwork", "CoreMotion",'AlipaySDK'
  s.library = 'sqlite3','c++','z'
end
