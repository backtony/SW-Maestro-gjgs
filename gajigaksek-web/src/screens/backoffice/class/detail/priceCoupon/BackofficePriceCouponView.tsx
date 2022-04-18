import React from "react";
import AdminLectureController from "../../../../../services/controllers/AdminLectureController";
import { filterNumber } from "../../../../../utils/commonParams";
import ClassAddNaviButton from "../../../../main/classAdd/ClassAddNaviButton";
import PriceBlock from "../../../../main/classAdd/priceCoupon/PriceBlock";
import Cookies from "universal-cookie";

const cookies = new Cookies();

interface BackofficePriceCouponViewStates {
  regularPrice: number;
  priceOne: number;
  priceTwo: number;
  priceThree: number;
  priceFour: number;
  couponPrice: number;
  couponCount: number;
}

export default class BackofficePriceCouponView extends React.Component<
  Record<string, never>,
  BackofficePriceCouponViewStates
> {
  constructor(props: any) {
    super(props);
    this.state = {
      regularPrice: 0,
      priceOne: 0,
      priceTwo: 0,
      priceThree: 0,
      priceFour: 0,
      couponPrice: 0,
      couponCount: 0,
    };

    this.getDetail(cookies.get("admin-lectureId"));
  }

  private async getDetail(lectureId: number) {
    try {
      const res = await AdminLectureController.getDetail(lectureId);
      const data = res?.data;
      this.setState({
        regularPrice: data.price.regularPrice,
        priceOne: data.price.priceOne,
        priceTwo: data.price.priceTwo,
        priceThree: data.price.priceThree,
        priceFour: data.price.priceFour,
        couponPrice: data.coupon.couponPrice,
        couponCount: data.coupon.couponCount,
      });
    } catch (e) {}
  }

  render() {
    return (
      <div className="class-add-first-container">
        <div className="class-add-first-header">
          <button
            className="class-add-first-back"
            onClick={() => (window.location.href = "/backoffice/class")}
          >
            <i className="fas fa-chevron-left fa-3x"></i>
          </button>
          <div className="class-add-first-header-title pretendard">
            클래스 검수
          </div>
        </div>
        <div className="class-add-first-navigation">
          <ClassAddNaviButton on={false} title={"기본정보"} />
          <ClassAddNaviButton on={false} title={"상세 소개"} />
          <ClassAddNaviButton on={false} title={"커리큘럼"} />
          <ClassAddNaviButton on={false} title={"스케줄"} />
          <ClassAddNaviButton on title={"가격 및 쿠폰"} />
        </div>
        <div className="class-add-first-main-container">
          <div className="class-add-first-name-container">
            <div className="class-add-mainCategory-title pretendard">가격</div>
            <div className="class-add-price-coupon-price-list">
              <PriceBlock
                price={this.state.regularPrice}
                title={"클래스 정가"}
                subTitle={""}
                onChangeText={
                  (text: string) => {
                    let dd;
                  }
                  // this.setState({
                  //   regularPrice: filterNumber(text),
                  // })
                }
              />
              <PriceBlock
                price={this.state.priceOne}
                title={"신청시 인당 가격"}
                subTitle={"1인"}
                onChangeText={
                  (text: string) => {
                    let dd;
                  }
                  // this.setState({
                  //   priceOne: filterNumber(text),
                  // })
                }
              />
              <PriceBlock
                price={this.state.priceTwo}
                title={"신청시 인당 가격"}
                subTitle={"2인"}
                onChangeText={
                  (text: string) => {
                    let dd;
                  }
                  // this.setState({
                  //   priceTwo: filterNumber(text),
                  // })
                }
              />
              <PriceBlock
                price={this.state.priceThree}
                title={"신청시 인당 가격"}
                subTitle={"3인"}
                onChangeText={
                  (text: string) => {
                    let dd;
                  }
                  // this.setState({
                  //   priceThree: filterNumber(text),
                  // })
                }
              />
              <PriceBlock
                price={this.state.priceFour}
                title={"신청시 인당 가격"}
                subTitle={"4인 이상"}
                onChangeText={
                  (text: string) => {
                    let dd;
                  }
                  // this.setState({
                  //   priceFour: filterNumber(text),
                  // })
                }
              />
            </div>
          </div>

          <div className="class-add-first-name-container">
            <div className="class-add-mainCategory-title pretendard">
              클래스 쿠폰 발급 신청
            </div>
            <div className="class-add-price-coupon-coupon-wrapper">
              <div className="class-add-price-coupon-coupon-title pretendard">
                쿠폰 할인 금액
              </div>
              <div className="class-add-price-coupon-coupon-price-wrapper">
                <input
                  className="wtf-coupon pretendard"
                  type="text"
                  value={this.state.couponPrice}
                  onChange={
                    (e: any) => {
                      let dd;
                    }
                    // this.setState({
                    //   couponPrice: filterNumber(e.target.value),
                    // })
                  }
                ></input>
                <div className="pretendard">원</div>
              </div>
            </div>

            <div className="class-add-price-coupon-coupon-wrapper">
              <div className="class-add-price-coupon-coupon-title pretendard">
                쿠폰 발급 인원
              </div>
              <div className="class-add-price-coupon-coupon-input-wrapper">
                <button
                  onClick={() => {
                    let dd;
                    // if (this.state.couponCount > 0)
                    //   this.setState({
                    //     couponCount: this.state.couponCount - 1,
                    //   });
                  }}
                >
                  <i className="fas fa-minus fa-lg"></i>
                </button>
                <input
                  className="wtf-coupon pretendard"
                  value={this.state.couponCount}
                ></input>
                명
                <button
                  onClick={
                    () => {
                      let dd;
                    }
                    // this.setState({ couponCount: this.state.couponCount + 1 })
                  }
                >
                  <i className="fas fa-plus fa-lg"></i>
                </button>
              </div>
              <div className="fucking-info pretendard">
                쿠폰은 발행일로 부터 30일간 사용 가능합니다.
              </div>
            </div>
          </div>
        </div>
        <div className="class-add-first-bottom">
          <div>
            <button
              className="class-add-intro-bottom-left pretendard"
              onClick={() =>
                (window.location.href = "/backoffice/class-detail/schedule")
              }
            >
              이전
            </button>
            <button
              className="class-add-first-bottom-left pretendard"
              onClick={() => (window.location.href = "/backoffice/class")}
            >
              다음
            </button>
          </div>
        </div>
      </div>
    );
  }
}
