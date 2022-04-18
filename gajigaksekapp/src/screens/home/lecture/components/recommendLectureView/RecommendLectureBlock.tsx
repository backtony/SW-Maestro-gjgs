import Zone from '@/utils/Zone';
import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import DropDownPicker from 'react-native-dropdown-picker';
import {Button} from 'react-native-elements';
import Icon from 'react-native-vector-icons/Ionicons';

interface RecommendLectureBlockProps {
  navigation: any;
  lecture: any;
  favorite: boolean;
  onPress: () => void;
}

interface RecommendLectureBlockStates {
  dropDown: boolean;
}

export default class RecommendLectureBlock extends React.Component<
  RecommendLectureBlockProps,
  RecommendLectureBlockStates
> {
  constructor(props: any) {
    super(props);
    this.state = {dropDown: false};
  }
  render() {
    return (
      <View style={styles.card}>
        <TouchableOpacity
          onPress={() => {
            this.props.navigation.navigate('lectureDash', {
              lectureId: this.props.lecture.lectureId,
            });
          }}>
          <View style={styles.imageContainer}>
            <Image
              style={styles.cardImage}
              source={{uri: this.props.lecture.imageUrl}}
            />
            <View style={this.styles.locationWrapper}>
              <Icon name="location-sharp" size={12} color="#FFF" />

              <Text style={styles.zone}>
                {Zone.getZone(this.props.lecture.zoneId)[1]}
              </Text>
            </View>
            <View style={{position: 'absolute', right: 0}}>
              <Button
                icon={
                  this.props.favorite ? (
                    <Icon name="heart" size={30} color="#ff4f4f" />
                  ) : (
                    <Icon name="heart-outline" size={30} color="#FFF" />
                  )
                }
                buttonStyle={{
                  backgroundColor: 'transparent',
                }}
                onPress={() => this.props.onPress()}
              />
            </View>
          </View>

          <View style={styles.cardContent}>
            <Text style={styles.title}>{this.props.lecture.title}</Text>
          </View>
        </TouchableOpacity>
        <DropDownPicker
          open={this.state.dropDown}
          value={'one'}
          items={[
            {
              label: `${this.props.lecture.priceOne}원`,
              value: 'one',
              icon: () => <Text style={{fontSize: 12}}>1인</Text>,
            },
            {
              label: `${this.props.lecture.priceTwo}원`,
              value: 'two',
              icon: () => <Text style={{fontSize: 12}}>2인</Text>,
            },
            {
              label: `${this.props.lecture.priceThree}원`,
              value: 'three',
              icon: () => <Text style={{fontSize: 12}}>3인</Text>,
            },
            {
              label: `${this.props.lecture.priceFour}원`,
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
          style={this.styles.dropDown}
          listItemContainerStyle={{height: 28}}
          listItemLabelStyle={{color: '#4a4a4c', fontSize: 12}} //dd
          dropDownContainerStyle={{
            borderWidth: 0,
          }}
          arrowIconStyle={this.styles.arrowIcon}
          textStyle={{fontSize: 14, fontWeight: 'bold'}}
        />
      </View>
    );
  }

  private styles = StyleSheet.create({
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
    dropDown: {
      backgroundColor: '#fafafb',
      borderWidth: 0,
      height: 34,
    },
    arrowIcon: {
      width: 18,
      height: 18,
    },
  });
}

const styles = StyleSheet.create({
  subCategory: {
    backgroundColor: '#2980b9',
    padding: 10,
    borderRadius: 5,
    margin: 5,
    alignContent: 'center',
    justifyContent: 'center',
  },
  subCategoryText: {
    fontSize: 20,
    textAlign: 'center',
  },
  container: {
    flex: 1,
    backgroundColor: '#FFF',
  },
  list: {
    paddingHorizontal: 10,
  },
  listContainer: {
    marginHorizontal: 10,
    marginTop: 10,
    // backgroundColor: 'red',
  },
  separator: {
    marginTop: 10,
  },
  /******** card **************/
  card: {
    // marginVertical: 8,
    backgroundColor: 'white',
    width: '45%',
    // flexBasis: '45%',
    marginHorizontal: 10,
    marginBottom: 15,
  },
  cardContent: {
    paddingTop: 7,
    paddingLeft: 4,
    justifyContent: 'space-between',
    // backgroundColor: 'skyblue',
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
    // flex: 1,
    color: '#FFF',

    // bottom: 0,
  },
  title: {
    // width: 93,
    // height: 20,
    fontFamily: 'NotoSansCJKkr-Bold',
    fontSize: 14,
    lineHeight: 16,
    color: '#070707',
    marginBottom: 4,
  },
  age: {
    fontFamily: 'NotoSansCJKkr-Regular',
    fontSize: 12,
    lineHeight: 14,
    color: '#4a4a4c',
    marginBottom: 4,
  },
  time: {
    fontSize: 18,
    flex: 1,
    color: '#778899',
  },
  people: {
    fontFamily: 'NotoSansCJKkr-Regular',
    fontSize: 14,
    lineHeight: 16,
    color: '#4a4a4c',
  },
  peopleBold: {
    fontFamily: 'NotoSansCJKkr-Bold',
    fontSize: 14,
    lineHeight: 16,
    color: '#4f6cff',
    marginLeft: 5,
  },
  count: {
    fontSize: 18,
    flex: 1,
    color: '#B0C4DE',
  },
});
