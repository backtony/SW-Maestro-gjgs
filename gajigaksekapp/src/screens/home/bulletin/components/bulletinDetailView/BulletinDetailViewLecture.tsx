import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {Button} from 'react-native-elements';
import DropDownPicker from 'react-native-dropdown-picker';
import Zone from '@/utils/Zone';
import FavoriteSelectModal from '@/screens/modal/FavoriteSelectModal';

interface BulletinDetailViewLectureProps {
  team: any;
  navigation: any;
  getBulletinDash: () => void;
}

interface BulletinDetailViewLectureStates {
  modalVisible: boolean;
  dropDown: boolean;
}

export default class BulletinDetailViewLecture extends React.Component<
  BulletinDetailViewLectureProps,
  BulletinDetailViewLectureStates
> {
  constructor(props: any) {
    super(props);
    this.state = {modalVisible: false, dropDown: false};
  }
  render() {
    return (
      <View>
        <View style={this.styles.titleWrapper}>
          <Text style={this.styles.text}>선택한 클래스</Text>
        </View>
        {this.props.team && (
          <View style={this.styles.card}>
            <TouchableOpacity
              onPress={() => {
                this.props.navigation.navigate('lectureDash', {
                  lectureId: this.props.team.bulletinsLecture.lectureId,
                });
              }}>
              <View style={this.styles.imageContainer}>
                <Image
                  style={this.styles.cardImage}
                  source={{
                    uri: this.props.team.bulletinsLecture.lecturesThumbnailUrl,
                  }}
                />
                <View style={this.styles.locationWrapper}>
                  <Icon name="location-sharp" size={12} color="#FFF" />

                  <Text style={this.styles.zone}>
                    {
                      Zone.getZone(
                        this.props.team.bulletinsLecture.lecturesZoneId,
                      )[1]
                    }
                  </Text>
                </View>
                <View style={{position: 'absolute', right: 0}}>
                  <Button
                    icon={
                      this.props.team.bulletinsLecture.myFavoriteLecture ? (
                        <Icon name="heart" size={30} color="#ff4f4f" />
                      ) : (
                        <Icon name="heart-outline" size={30} color="#FFF" />
                      )
                    }
                    buttonStyle={{
                      backgroundColor: 'transparent',
                    }}
                    onPress={() => {
                      this.setState({
                        modalVisible: !this.state.modalVisible,
                      });
                    }}
                  />
                </View>
              </View>

              <View style={this.styles.cardContent}>
                <Text style={this.styles.title}>
                  {this.props.team.bulletinsLecture.lectureName}
                </Text>
              </View>
            </TouchableOpacity>
            <DropDownPicker
              open={this.state.dropDown}
              value={'one'}
              items={[
                {
                  label: '50000원',
                  value: 'one',
                  icon: () => <Text style={{fontSize: 12}}>1인</Text>,
                },
                {
                  label: '40000원',
                  value: 'two',
                  icon: () => <Text style={{fontSize: 12}}>2인</Text>,
                },
                {
                  label: '30000원',
                  value: 'three',
                  icon: () => <Text style={{fontSize: 12}}>3인</Text>,
                },
                {
                  label: '20000원',
                  value: 'four',
                  icon: () => <Text style={{fontSize: 12}}>4인 이상</Text>,
                },
              ]}
              setOpen={() => {
                this.setState({dropDown: !this.state.dropDown});
              }}
              setValue={() => {}}
              setItems={() => {}}
              showTickIcon={false}
              dropDownDirection="TOP"
              style={this.styles.dropDownPickerStyle}
              listItemContainerStyle={{height: 28}}
              listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}} //dd
              dropDownContainerStyle={this.styles.dropDownContainerStyle}
              arrowIconStyle={this.styles.arrowIconStyle}
              textStyle={{fontSize: 14, fontWeight: 'bold'}}
            />
          </View>
        )}
        <View>
          {this.props.team && (
            <FavoriteSelectModal
              modalVisible={this.state.modalVisible}
              lecture={{
                lectureId: this.props.team.bulletinsLecture.lectureId,
                imageUrl: this.props.team.bulletinsLecture.lecturesThumbnailUrl,
                zoneId: this.props.team.bulletinsLecture.lecturesZoneId,
                title: this.props.team.bulletinsLecture.lectureName,
                priceOne: this.props.team.bulletinsLecture.priceOne,
                priceTwo: this.props.team.bulletinsLecture.priceTwo,
                priceThree: this.props.team.bulletinsLecture.priceThree,
                priceFour: this.props.team.bulletinsLecture.priceFour,
                myFavorite: this.props.team.bulletinsLecture.myFavoriteLecture,
              }}
              setModalVisible={(visible: boolean) => {
                this.setState({modalVisible: visible});
              }}
              setFavorite={() => {
                this.props.getBulletinDash();
              }}
            />
          )}
        </View>
      </View>
    );
  }

  private styles = StyleSheet.create({
    /******** card **************/
    card: {
      backgroundColor: 'white',
      width: 152,
      marginRight: 10,
      marginBottom: 15,
    },
    cardContent: {
      paddingTop: 7,
      paddingLeft: 4,
      justifyContent: 'space-between',
    },
    cardImage: {
      flex: 1,
      height: 122,
      borderRadius: 9,
    },
    imageContainer: {
      borderRadius: 10,
    },
    /******** card components **************/
    zone: {
      fontSize: 10,
      color: '#FFF',
    },
    title: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 14,
      lineHeight: 16,
      color: '#070707',
      marginBottom: 4,
    },
    titleWrapper: {
      flexDirection: 'row',
      marginTop: 40,
      alignItems: 'center',
      marginBottom: 20,
    },
    text: {
      fontSize: 16,
      lineHeight: 18,
      fontFamily: 'NotoSansCJKkr-Bold',
      marginTop: 7,
      marginLeft: 6,
    },
    locationWrapper: {
      position: 'absolute',
      bottom: 0,
      flexDirection: 'row',
      borderRadius: 9,
      backgroundColor: 'rgba(0, 0, 0, 0.3)',
      padding: 2,
      paddingHorizontal: 5,
      margin: 8,
    },
    dropDownPickerStyle: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
    },
    dropDownContainerStyle: {
      borderWidth: 0,
    },
    arrowIconStyle: {
      width: 18,
      height: 18,
    },
  });
}
