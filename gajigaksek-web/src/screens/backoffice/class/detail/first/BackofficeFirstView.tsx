import React from "react";
import ClassAddGeneralButton from "../../../../main/classAdd/ClassAddGeneralButton";
import ClassAddNaviButton from "../../../../main/classAdd/ClassAddNaviButton";
import Category from "../../../../../utils/Category";
import AdminLectureController from "../../../../../services/controllers/AdminLectureController";
import Cookies from "universal-cookie";

const cookies = new Cookies();

interface BackofficeFirstViewStates {
  mainCategory: number;
  subCategory: number;
  title: string;
  imgOn: boolean;
  address: string;
}

export default class BackofficeFirstView extends React.Component<
  Record<string, never>,
  BackofficeFirstViewStates
> {
  private imgRef;

  constructor(props: any) {
    super(props);
    this.state = {
      mainCategory: 1,
      subCategory: 2,
      title: "",
      imgOn: false,
      address: "",
    };
    this.imgRef = React.createRef<HTMLImageElement>();
    this.getFirst(+cookies.get("admin-lectureId"));
  }

  private async getFirst(lectureId: number) {
    try {
      const res = await AdminLectureController.getDetail(lectureId);
      const data = res?.data;
      this.setState({
        mainCategory: Category.getMainId(data.categoryId),
        subCategory: data.categoryId,
        title: data.title,
        address: data.address,
      });
      if (this.imgRef.current) {
        this.imgRef.current.src = data.thumbnailImageFileUrl;
        this.setState({ imgOn: true });
      }
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
          <ClassAddNaviButton on title={"기본정보"} />
          <ClassAddNaviButton on={false} title={"상세 소개"} />
          <ClassAddNaviButton on={false} title={"커리큘럼"} />
          <ClassAddNaviButton on={false} title={"스케줄"} />
          <ClassAddNaviButton on={false} title={"가격 및 쿠폰"} />
          <ClassAddNaviButton on={false} title={"부가정보"} />
        </div>
        <div className="class-add-first-main-container">
          <div className="class-add-mainCategory-container">
            <div className="class-add-mainCategory-title pretendard">
              대분류
            </div>
            <div className="class-add-mainCategory-button-container">
              {Category.getCategoryList().map((big) => (
                <ClassAddGeneralButton
                  title={big}
                  onClick={
                    () => {
                      let dd;
                    }
                    // this.setState({ mainCategory: Category.getId(big, "") })
                  }
                  on={this.state.mainCategory === Category.getId(big, "")}
                />
              ))}
            </div>
          </div>
          <div className="class-add-subCategory-container">
            <div className="class-add-mainCategory-title pretendard">
              소분류
            </div>
            <div className="class-add-mainCategory-button-container">
              {Category.getSubCategoryList(
                Category.getCategory(this.state.mainCategory)[0]
              ).map((sub) => (
                <ClassAddGeneralButton
                  title={sub.sub}
                  onClick={() => {
                    let dd;
                  }} //this.setState({ subCategory: +sub.id })}
                  on={this.state.subCategory === +sub.id}
                />
              ))}
            </div>
          </div>

          <div className="class-add-first-name-container">
            <div className="class-add-mainCategory-title pretendard">
              클래스 제목
            </div>
            <div className="class-add-first-name-wrapper">
              <input
                className="class-add-first-name-input pretendard"
                placeholder="클래스 제목"
                value={this.state.title}
                onChange={
                  (event: any) => {
                    let dd;
                  }
                  // this.setState({ title: event.target.value })
                }
              ></input>
            </div>
          </div>

          <div className="class-add-first-img-container">
            <div className="class-add-mainCategory-title pretendard">
              대표 사진
            </div>
            <div className="class-add-first-img-wrapper">
              <button
                className="class-add-first-img-button"
                onClick={() => alert("조회만 가능합니다.")}
              >
                {!this.state.imgOn && (
                  <div>
                    <i className="fas fa-plus fa-2x"></i>

                    <div className="class-add-first-img-button-text pretendard">
                      이미지 추가
                    </div>
                  </div>
                )}
                <input
                  type="file"
                  className="class-add-first-img-input"
                  accept="image/png, image/jpeg"
                ></input>

                <img
                  className={`class-add-first-img ${
                    this.state.imgOn ? "" : "display-none"
                  }`}
                  ref={this.imgRef}
                  src=""
                />
              </button>
            </div>
          </div>

          <div className="class-add-first-address-container">
            <div className="class-add-mainCategory-title pretendard">주소</div>
            <div className="class-add-first-name-wrapper">
              <input
                className="class-add-first-name-input pretendard"
                placeholder="주소 입력"
                value={this.state.address}
                onChange={
                  (event: any) => {
                    let dd;
                  }
                  // this.setState({ address: event.target.value })
                }
              ></input>
            </div>
          </div>
        </div>
        <div className="class-add-first-bottom">
          <button
            className="class-add-first-bottom-left pretendard"
            onClick={() => {
              window.location.href = "/backoffice/class-detail/intro";
            }}
          >
            다음
          </button>
        </div>
      </div>
    );
  }
}
