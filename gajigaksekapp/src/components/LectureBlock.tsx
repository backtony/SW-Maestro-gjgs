import React from 'react';
import {Alert, Image, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {Button} from 'react-native-elements';
import DropDownPicker from 'react-native-dropdown-picker';
import Zone from '@/utils/Zone';
import UserDC from '@/services/login/UserDC';
import FavoriteApiController from '@/services/apis/FavoriteApiController';
import {StyleSheet} from 'react-native';

interface LectureBlockProps {
  navigation: any;
  lecture: any;
  refreshParent: () => void;
  deleteLecture: (lectureId: number) => void;
}

interface LectureBlockStates {
  dropDown: boolean;
}

export default class LectureBlock extends React.Component<
  LectureBlockProps,
  LectureBlockStates
> {
  constructor() {
    super();
    this.state = {dropDown: false};
  }

  render() {
    return (
      <View style={this.styles.card}>
        <TouchableOpacity
          onPress={() => {
            this.props.navigation.navigate('lectureDash', {
              lectureId: this.props.lecture.lectureId,
            });
          }}>
          <View style={this.styles.imageContainer}>
            <Image
              style={this.styles.cardImage}
              source={{uri: this.props.lecture.thumbnailImageFileUrl}}
            />
            <View style={this.styles.zoneContainer}>
              <Icon name="location-sharp" size={12} color="#FFF" />

              <Text style={this.styles.zone}>
                {Zone.getZone(this.props.lecture.zoneId)[1]}
              </Text>
            </View>
            <View style={this.styles.heartContainer}>
              <Button
                icon={<Icon name="heart" size={30} color="#ff4f4f" />}
                buttonStyle={this.styles.button}
                onPress={() => {
                  if (UserDC.isLogout()) {
                    Alert.alert('로그인 해주세요.');
                    return;
                  }
                  this.props.deleteLecture(this.props.lecture.lectureId);
                }}
              />
            </View>
          </View>

          <View style={this.styles.cardContent}>
            <Text style={this.styles.title}>{this.props.lecture.title}</Text>
          </View>
        </TouchableOpacity>
        <DropDownPicker
          open={this.state.dropDown}
          value={'one'}
          items={[
            {
              label: `${this.props.lecture.price.priceOne}원`,
              value: 'one',
              icon: () => <Text style={this.styles.text}>1인</Text>,
            },
            {
              label: `${this.props.lecture.price.priceTwo}원`,
              value: 'two',
              icon: () => <Text style={this.styles.text}>2인</Text>,
            },
            {
              label: `${this.props.lecture.price.priceThree}원`,
              value: 'three',
              icon: () => <Text style={this.styles.text}>3인</Text>,
            },
            {
              label: `${this.props.lecture.price.priceFour}원`,
              value: 'four',
              icon: () => <Text style={this.styles.text}>4인 이상</Text>,
            },
          ]}
          setOpen={() => {
            this.setState({dropDown: !this.state.dropDown});
          }}
          setValue={() => {}}
          setItems={() => {}}
          showTickIcon={false}
          dropDownDirection="TOP"
          style={this.styles.dropDown}
          listItemContainerStyle={this.styles.listItemContainer}
          listItemLabelStyle={this.styles.listItemLabel} //dd
          dropDownContainerStyle={this.styles.dropDownContainer}
          arrowIconStyle={this.styles.arrowIcon}
          textStyle={this.styles.dropDownText}
        />
      </View>
    );
  }

  private styles = StyleSheet.create({
    card: {
      backgroundColor: 'white',
      width: '45%',
      marginHorizontal: 10,
      marginBottom: 15,
    },
    cardImage: {
      flex: 1,
      height: 122,
      borderRadius: 9,
    },
    imageContainer: {
      borderRadius: 10,
    },
    zone: {
      fontSize: 10,
      color: '#FFF',
    },
    cardContent: {
      paddingTop: 7,
      paddingLeft: 4,
      justifyContent: 'space-between',
    },
    title: {
      fontFamily: 'NotoSansCJKkr-Bold',
      fontSize: 14,
      lineHeight: 16,
      color: '#070707',
      marginBottom: 4,
    },
    zoneContainer: {
      position: 'absolute',
      bottom: 0,
      flexDirection: 'row',
      borderRadius: 9,
      backgroundColor: 'rgba(0, 0, 0, 0.3)',
      padding: 2,
      paddingHorizontal: 5,
      margin: 8,
    },
    button: {
      backgroundColor: 'transparent',
    },
    text: {fontSize: 12},
    dropDown: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
    },
    listItemContainer: {height: 28},
    listItemLabel: {color: '#4a4a4c', fontSize: 12},
    dropDownContainer: {
      borderWidth: 0,
    },
    arrowIcon: {
      width: 18,
      height: 18,
    },
    dropDownText: {fontSize: 14, fontWeight: 'bold'},
    heartContainer: {position: 'absolute', right: 0},
  });
}
