#
#  Be sure to run `pod spec lint RCTWeChat.podspec' to ensure this is a
#  valid spec and to remove all comments including this before submitting the spec.
#
#  To learn more about Podspec attributes see http://docs.cocoapods.org/specification.html
#  To see working Podspecs in the CocoaPods repo see https://github.com/CocoaPods/Specs/
#

Pod::Spec.new do |s|
  s.name         = "LnPay"
  s.version      = "1.0.0"
  s.summary      = "Alipay,Wechat SDK for React Native. "
  s.description  = <<-DESC
  Alipay,Wechat SDK for React Native.
   DESC
  s.author       = { "yorkie" => "yorkiefixer@gmail.com" }
  s.homepage     = "https://github.com/ln1778/ln1778-wechat.git"
  s.license      = "MIT"
  s.platform     = :ios, "12.4"
  s.source       = { :git => "https://github.com/ln1778/ln1778-wechat.git", :tag => "master" }
  s.source_files  = "ios/*.{h,m}"
  s.resource = 'AlipaySDK.bundle'
  s.dependency "React"
  s.vendored_libraries = "ios/libWechatOpenSDK.a","libAlipaySDK.a"
  s.requires_arc = true
  s.frameworks = 'SystemConfiguration','CoreTelephony, "QuartzCore", "CoreText", "CoreGraphics", "UIKit", "Foundation", "CFNetwork", "CoreMotion'
  s.library = 'sqlite3','c++','z'
end
