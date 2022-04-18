import React from 'react';
import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import Icon from 'react-native-vector-icons/Ionicons';
import {Button} from 'react-native-elements';
import Zone from '@/utils/Zone';
import {englishAge, englishTime} from '@/utils/commonParam';

interface BulletinMainViewBulletinBlockProps {
  navigation: any;
  bulletinInfo: any;
  refreshParent: () => void;
  postFavorite: (bulletinInfo: any) => void;
  deleteFavorite: (bulletinInfo: any) => void;
}

export default class BulletinMainViewBulletinBlock extends React.Component<
  BulletinMainViewBulletinBlockProps,
  {}
> {
  render() {
    return (
      <TouchableOpacity
        style={this.styles.card}
        onPress={() =>
          this.props.navigation.navigate('bulletinDash', {
            bulletinId: this.props.bulletinInfo.bulletinId,
            myFavorite: this.props.bulletinInfo.myFavorite,
            refreshParent: () => this.props.refreshParent(),
          })
        }>
        <View style={this.styles.imageContainer}>
          <Image
            style={this.styles.cardImage}
            source={{uri: this.props.bulletinInfo.lectureImageUrl}}
          />
          <View style={this.styles.locationWrapper}>
            <Icon name="location-sharp" size={12} color="#FFF" />
            <Text style={this.styles.zone}>
              {Zone.getZone(this.props.bulletinInfo.zoneId)[1]}
            </Text>
          </View>
        </View>
        <View style={this.styles.cardContent}>
          <Text style={this.styles.title}>
            {this.props.bulletinInfo.bulletinTitle}
          </Text>
          <Text style={this.styles.age}>
            {englishAge[this.props.bulletinInfo.age]} ·{' '}
            {this.props.bulletinInfo.time
              .split('|')
              .map(tt => englishTime[tt])
              .join('/')}
          </Text>
          <View style={{flexDirection: 'row', alignItems: 'center'}}>
            <Icon name="person" size={14} color="#4f6cff" />
            <Text style={this.styles.peopleBold}>
              {this.props.bulletinInfo.nowMembers}
            </Text>
            <Text style={this.styles.people}>
              /{this.props.bulletinInfo.maxMembers}
            </Text>
          </View>
        </View>

        <View style={{position: 'absolute', right: 0}}>
          <Button
            icon={
              this.props.bulletinInfo.myFavorite ? (
                <Icon name="heart" size={30} color="#ff4f4f" />
              ) : (
                <Icon name="heart-outline" size={30} color="#FFF" />
              )
            }
            buttonStyle={{
              backgroundColor: 'transparent',
            }}
            onPress={() => {
              if (!this.props.bulletinInfo.myFavorite) {
                this.props.postFavorite(this.props.bulletinInfo);
              } else if (this.props.bulletinInfo.myFavorite) {
                this.props.deleteFavorite(this.props.bulletinInfo);
              }
            }}
          />
        </View>
      </TouchableOpacity>
    );
  }

  private styles = StyleSheet.create({
    /******** card **************/
    card: {
      backgroundColor: 'white',
      width: '45%',
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
    age: {
      fontFamily: 'NotoSansCJKkr-Regular',
      fontSize: 12,
      lineHeight: 14,
      color: '#4a4a4c',
      marginBottom: 4,
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
  });
}
